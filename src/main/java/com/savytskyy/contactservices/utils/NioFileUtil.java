package com.savytskyy.contactservices.utils;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NioFileUtil {
    private final Path filePath;

    public void readByLine(Consumer<String> onLine) {
        try(ByteChannel channel = Files.newByteChannel(filePath, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(100);

            String stringBuffer ="";
            while (channel.read(buffer)!=-1) {
                buffer.flip();

                stringBuffer += StandardCharsets.UTF_8.decode(buffer).toString();
                //System.out.println("stringBuffer"+stringBuffer);
                String[] parts = stringBuffer.split("\\n");
                for (int i = 0; i < parts.length-1; i++) {
                    onLine.accept(parts[i]);
                }
                stringBuffer = parts[parts.length-1];
                buffer.clear();
            }
            onLine.accept(stringBuffer);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromBuffer(ByteBuffer buffer) {
        return new String(buffer.array(), buffer.position(), buffer.limit());
    }
}
