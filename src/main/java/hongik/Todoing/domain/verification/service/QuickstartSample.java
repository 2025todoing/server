package hongik.Todoing.domain.verification.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuickstartSample {
    public static void main(String[] args) throws Exception{
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()){

            // image Source
            String fileName ="src/main/resources/sample.jpg";

            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder()
                    .setContent(imgBytes)
                    .build();
            Feature feat = Feature.newBuilder()
                    .setType(Feature.Type.LABEL_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat).build();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for(AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for(EntityAnnotation annotation: res.getLabelAnnotationsList()) {
                    annotation.getAllFields()
                            .forEach((k, v) -> System.out.format(
                                    "Annotation: %s : %s%n", k, v.toString()));
                }
            }
        }
    }
}
