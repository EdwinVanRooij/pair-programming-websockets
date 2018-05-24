# Use a python base image, building atop of that one
FROM edwinvanrooij/myubuntu

# Set current working dir
WORKDIR /app

# Copy the current dir into the newly created directory /app under the root filesystem in image
ADD . /app

# Set port open to outside container
EXPOSE 8086

# Run command when container launches
CMD ["java", "-jar", "build/libs/pair-programming-websockets.jar"]
