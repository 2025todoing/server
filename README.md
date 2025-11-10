# todoing
[2025 졸업프로젝트 투둥이]


## terraform 으로 인프라를 코드로 관리

### 명령어 모음집
- terraform init
- terraform plan
- terraform apply
- terraform destroy

- ssh 접속
    - ssh -i todoing-key.pem ubuntu@3.38.113.231

### ssh 들어간 후 명령어 모음집


- docker 현재 pw가 mask가 되어 있지 않은 문제 발생

      # === 배포(SSH 유지): ECR에서 pull 후 컨테이너 실행 ===
      - name: Deploy to EC2 (SSH)
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.EC2_HOST_IP }}
          username: ${{ secrets.EC2_NAME }}
          key: ${{ secrets.EC2_KEY }}
          port: 22
          script: |
            set -e
            # ECR 로그인 (EC2에 AWS CLI 필요 / 인스턴스 롤 권장)
            aws ecr get-login-password --region ${{ env.AWS_REGION }} \
              | docker login --username AWS --password-stdin ${{ steps.login-ecr.outputs.registry }}
            
            docker stop hongik-todoing || true
            docker rm hongik-todoing || true
            
            docker pull ${{ env.IMAGE_URI }}d
            
            docker run -d --name hongik-todoing -p 8080:8080 \
              -e SPRING_PROFILES_ACTIVE=dev \
              -e SPRING_DATASOURCE_URL=${{ secrets.SPRING_DATASOURCE_URL }} \
              -e SPRING_DATASOURCE_USERNAME=${{ secrets.SPRING_DATASOURCE_USERNAME }} \
              -e SPRING_DATASOURCE_PASSWORD=${{ secrets.SPRING_DATASOURCE_PASSWORD }} \
              -e JWT_SECRET=${{ secrets.JWT_SECRET }} \
              -e JWT_ACCESS_EXPIRE=${{ secrets.JWT_ACCESS_EXPIRE }} \
              -e JWT_REFRESH_EXPIRE=${{ secrets.JWT_REFRESH_EXPIRE }} \
              -e KAKAO_CLIENT_ID=${{ secrets.KAKAO_CLIENT_ID }} \
              -e KAKAO_REDIRECT_URI=${{ secrets.KAKAO_REDIRECT_URI }} \
              -e OPENAI_API_KEY=${{ secrets.OPENAI_API_KEY }} \
              ${{ env.IMAGE_URI }}
            
            
            

