# Sử dụng hình ảnh môi trường Java
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc của ứng dụng trong container
WORKDIR /app

# Sao chép các file jar vào thư mục /app trong container
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose cổng 8080 của container
EXPOSE 8080

# Lệnh chạy ứng dụng Spring Boot khi container được khởi chạy
CMD ["java", "-jar", "demo.jar"]