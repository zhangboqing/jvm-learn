package f_asm_and_javassist.addField;

import java.io.*;

/**
 * @Author zhangboqing
 * @Date 2020/3/27
 */
public class FileUtils {

    public static byte[] readFileToByteArray(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException(filename);
        }
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int bufferLength = 1024;
            byte[] buffer = new byte[bufferLength];
            int len = bufferedInputStream.read(buffer, 0, bufferLength);
            while ( len != -1) {
                byteArrayOutputStream.write(buffer,0,len);
                len = bufferedInputStream.read(buffer, 0, bufferLength);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeByteArrayToFile(String fileName, byte[] bytesModified) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytesModified);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
