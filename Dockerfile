# Sử dụng hình ảnh môi trường Java
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc của ứng dụng trong container
WORKDIR /app

# Sao chép file GeoLite2-City.mmdb từ thư mục src/main/resources vào thư mục đích trong container
COPY src/main/resources/GeoLite2-City.mmdb /app/resources/GeoLite2-City.mmdb

# Sao chép thư mục target vào thư mục đích trong container
COPY target /app/target

# Expose cổng 8080 của container
EXPOSE 8080

# Lệnh chạy ứng dụng Spring Boot khi container được khởi chạy
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]