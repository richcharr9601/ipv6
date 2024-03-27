# Sử dụng một hình ảnh cơ sở chứa Java
FROM openjdk:17-jdk-slim

# Tạo thư mục /app trong container
WORKDIR /app

# Thêm tệp jar của ứng dụng vào thư mục /app trong hình ảnh
COPY target/Demouploadanddownload-0.0.1-SNAPSHOT.jar /app/app.jar

# Port mà ứng dụng sử dụng
EXPOSE 8080

# Lệnh chạy ứng dụng khi container được khởi chạy
CMD ["java", "-jar", "app.jar"]