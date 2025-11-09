# Provider settings
provider "aws" {
  region = "ap-northeast-2"
}

# Create a VPC
resource "aws_vpc" "main" {
    cidr_block           = "10.0.0.0/16"
    enable_dns_hostnames = true
    enable_dns_support   = true

    tags = {
      Name = "todoing-vpc"
    }
}

# Internet Gateway
resource "aws_internet_gateway" "main" {
    vpc_id = aws_vpc.main.id

    tags = {
      Name = "todoing-igw"
    }
}

# Public Subnet 1
resource "aws_subnet" "public_1" {
    vpc_id            = aws_vpc.main.id
    cidr_block        = "10.0.3.0/24"
    availability_zone = "ap-northeast-2a"
    map_public_ip_on_launch = true

    tags = {
      Name = "todoing-public-subnet-1"
    }
}

# Public Subnet 2
resource "aws_subnet" "public_2" {
    vpc_id            = aws_vpc.main.id
    cidr_block        = "10.0.4.0/24"
    availability_zone = "ap-northeast-2c"
    map_public_ip_on_launch = true

    tags = {
      Name = "todoing-public-subnet-2"
    }
}

# Public Route Table
resource "aws_route_table" "public" {
    vpc_id = aws_vpc.main.id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.main.id
    }

    tags = {
      Name = "todoing-public-rt"
    }
}

# Associate Public Subnet 1 with Public Route Table
resource "aws_route_table_association" "public_1_association" {
    subnet_id      = aws_subnet.public_1.id
    route_table_id = aws_route_table.public.id
}

# Associate Public Subnet 2 with Public Route Table
resource "aws_route_table_association" "public_2_association" {
    subnet_id      = aws_subnet.public_2.id
    route_table_id = aws_route_table.public.id
}

# SG for EC2
resource "aws_security_group" "ec2" {
    name        = "todoing-ec2-sg"
    description = "Security group for Todoing EC2 instances"
    vpc_id      = aws_vpc.main.id

    # Spring Boot Application Port
    ingress {
        from_port = 8080
        to_port   = 8080
        protocol  = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "Allow inbound traffic on port 8080"
    }

    # SSH Access
    ingress {
        from_port   = 22
        to_port     = 22
        protocol    = "tcp"
        cidr_blocks  = ["0.0.0.0/0"]
    }

    # Inbound HTTP Access
    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks  = ["0.0.0.0/0"]
    }

    tags = {
      Name = "todoing-ec2-sg"
    }
}


# IAM Role for EC2
resource "aws_iam_role" "ec2_role" {
    name = "todoing-ec2-role"

    assume_role_policy = jsonencode({
      Version = "2012-10-17"
      Statement = [
        {
          Action = "sts:AssumeRole"
          Effect = "Allow"
          Principal = {
            Service = "ec2.amazonaws.com"
          }
        }
      ]
    })
}


# SSM Policy Attachment
resource "aws_iam_role_policy_attachment" "ssm_policy" {
    role       = aws_iam_role.ec2_role.name
    policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

# EC2 Instance Profile
resource "aws_iam_instance_profile" "ec2_instance_profile" {
    name = "todoing-ec2-instance-profile"
    role = aws_iam_role.ec2_role.name
}

# EC2 Instance
resource "aws_instance" "app" {
    ami                         = "ami-0e9bfdb247cc8de84" # Amazon Ubuntu 로 수정했습니다.
    instance_type               = "t2.micro"
    subnet_id                   = aws_subnet.public_1.id

    # monitoring with CloudWatch Agent
    monitoring                  = false # 프리티어를 위해서 현재 false로 설정했습니다.

    vpc_security_group_ids      = [aws_security_group.ec2.id]
    key_name                  = "todoing-key"  # Replace with your actual key pair name
    iam_instance_profile        = aws_iam_instance_profile.ec2_instance_profile.name

    root_block_device {
        volume_size = 30
        volume_type = "gp2"
    }

    user_data = <<-EOF
                #!/bin/bash
                set -e  # 오류 발생 시 스크립트 중단

                # SSM Agent 설치
                sudo snap install amazon-ssm-agent --classic
                sudo systemctl start snap.amazon-ssm-agent.amazon-ssm-agent.service
                sudo systemctl enable snap.amazon-ssm-agent.amazon-ssm-agent.service

                # AWS CLI 설치
                sudo apt-get update
                sudo apt-get install -y unzip
                curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                unzip awscliv2.zip
                sudo ./aws/install

                # Docker 설치
                sudo apt-get install -y ca-certificates curl gnupg
                sudo install -m 0755 -d /etc/apt/keyrings
                curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
                sudo chmod a+r /etc/apt/keyrings/docker.gpg
                echo "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
                sudo apt-get update
                sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
                sudo usermod -aG docker ubuntu
                sudo systemctl start docker
                sudo systemctl enable docker

                # Docker Compose 설치
                sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                sudo chmod +x /usr/local/bin/docker-compose
                EOF

    tags = {
      Name = "todoing-app-instance"
    }
}

# Elastic IP
resource "aws_eip" "app" {
    instance = aws_instance.app.id

    tags = {
      Name = "todoing-app-eip"
    }
}

#Output
output "public_ip" {
    description = "Public IP address of the EC2 instance"
    value       = aws_eip.app.public_ip
}

output "ssh_command" {
    description = "SSH command to connect to the EC2 instance"
    value       = "ssh -i todoing-key.pem ubuntu@${aws_eip.app.public_ip}"
}

output "instance_id" {
    description = "ID of the EC2 instance"
    value       = aws_instance.app.id
}

# ECR Repository
resource "aws_ecr_repository" "app" {
    name = "todoing-repo"
    force_delete = true // Added

    image_scanning_configuration {
      scan_on_push = true
    }

    tags = {
      Name = "todoing-ecr-repo"
    }
}

# ECR 저장소 정책이 없어 에러가 생겨 추가하였습니다.
resource "aws_ecr_repository_policy" "app_policy" {
    repository = aws_ecr_repository.app.name

    policy = jsonencode({
      Version = "2008-10-17"
      Statement = [
        {
          Sid = "AllowPushPull"
          Effect = "Allow"
          Principal = "*"
          Action = [
            "ecr:GetDownloadUrlForLayer",
            "ecr:BatchGetImage",
            "ecr:BatchCheckLayerAvailability",
            "ecr:PutImage",
            "ecr:InitiateLayerUpload",
            "ecr:UploadLayerPart",
            "ecr:CompleteLayerUpload"
          ]
        }
      ]
    })
}

# Key pair generation
resource "aws_key_pair" "todoing" {
    key_name   = "todoing-key"
    public_key = file("${path.module}/todoing-key.pub")  # 이제 필요할 때만 배포할 것이기 때문에 로컬에 public key 경로로 지정

}

# RDS SG
resource "aws_security_group" "rds" {
    name        = "todoing-rds-sg"
    description = "Security group for Todoing RDS"
    vpc_id      = aws_vpc.main.id

    # 외부의 MYSQL 접속 허용
    ingress {
        from_port   = 3306
        to_port     = 3306
        protocol    = "tcp"
        cidr_blocks  = ["0.0.0.0/0"] # 현재 모든 IP 에서 접근 가능케 했습니다. (협업용)
    }

    tags = {
      Name = "todoing-rds-sg"
    }
}

# RDS Subnet Group
resource "aws_db_subnet_group" "rds" {
    name       = "todoing-rds-subnet-group"
    subnet_ids = [aws_subnet.public_1.id, aws_subnet.public_2.id]

    tags = {
      Name = "todoing-rds-subnet-group"
    }
}

# RDS 향상된 모니터링을 위한 IAM 역할 <- 추후에 추가하도록 하겠습니다.

# RDS 인스턴스
resource "aws_db_instance" "todoing" {
  identifier           = "todoing-db"
  engine              = "mysql"
  engine_version      = "8.0"
  instance_class      = "db.t3.micro"
  allocated_storage   = 20
  storage_type        = "gp2"

  db_name             = "todoing"
  username           = "todoing_user"
  password           = var.db_password

  vpc_security_group_ids = [aws_security_group.rds.id]
  db_subnet_group_name   = aws_db_subnet_group.rds.name

  skip_final_snapshot    = true
  publicly_accessible    = true


  tags = {
    Name = "todoing-db"
  }
}

# RDS 엔드포인트 출력
output "rds_endpoint" {
  value = aws_db_instance.todoing.endpoint
}
