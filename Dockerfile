# Sử dụng một hình ảnh cơ sở chứa Java
FROM openjdk:17-jdk-slim

# Thêm tệp jar của ứng dụng vào hình ảnh
ADD target/Demouploadanddownload-0.0.1-SNAPSHOT.jar app.jar

# Port mà ứng dụng sử dụng
EXPOSE 8080

# Lệnh chạy ứng dụng khi container được khởi chạy
ENTRYPOINT ["java","-jar","/app.jar"]