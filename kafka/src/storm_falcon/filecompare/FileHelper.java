package storm_falcon.filecompare;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public class FileHelper {

    /**
     * 下载文件
     *
     * @param strUrl     网络url
     * @param pathToSave 本地保存路径
     * @return
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void download(String strUrl, String pathToSave) {
        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            File file = createFile(pathToSave);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);

            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按字节读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static byte[] readFile(File file) throws IOException {
        long len = file.length();
        byte data[] = new byte[(int) len];
        FileInputStream fin = new FileInputStream(file);
        int r = fin.read(data);
        if (r != len)
            throw new IOException("Only read " + r + " of " + len + " for " + file);
        fin.close();
        return data;
    }

    /**
     * 按字节读取文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    @SuppressWarnings({"UnusedDeclaration"})
    static public byte[] readFile(String filename) throws IOException {
        File file = new File(filename);
        return readFile(file);
    }

    /**
     * 将byte写入文件
     *
     * @param file
     * @param data
     * @throws IOException
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void writeFile(File file, byte[] data) throws IOException {
        FileOutputStream fOut = new FileOutputStream(file);
        fOut.write(data);
        fOut.close();
    }

    /**
     * 将byte写入文件
     *
     * @param filename
     * @param data
     * @throws IOException
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void writeFile(String filename, byte data[]) throws IOException {
        FileOutputStream fout = new FileOutputStream(filename);
        fout.write(data);
        fout.close();
    }

    /**
     * 遍历目录
     *
     * @param dirPath    目标目录
     * @param isFullName 是否为完整路径
     * @param suffixes   筛选后缀名，即文件格式
     * @return 所有符合格式的文件名
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static List<String> traversalDir(String dirPath, boolean isFullName, String... suffixes) {
        List<String> fileList = new ArrayList<>();
        File root = new File(dirPath);
        addFileToList(root, fileList, isFullName, suffixes);
        return fileList;
    }

    private static void addFileToList(File dirFile, List<String> fileList, boolean isFullName, String... suffixes) {
        File[] fs = dirFile.listFiles();

        for (File file : fs != null ? fs : new File[0]) {
            boolean flag = false;
            String path = file.getAbsolutePath();

            //判断文件格式
            if (suffixes.length == 0) {
                flag = true;
            }
            for (String suffix : suffixes) {
                if (path.endsWith(suffix)) {
                    flag = true;
                    break;
                }
            }

            if (flag && !file.isDirectory()) {
                if (isFullName) {
                    fileList.add(file.getAbsolutePath());
                } else {
                    fileList.add(file.getName());
                }
            } else if (file.isDirectory()) {
                addFileToList(file, fileList, isFullName, suffixes);
            }
        }
    }

    /**
     * 文件不存在则创建新文件
     *
     * @param strFilePath
     * @return 返回创建的文件对象
     */
    public static File createFile(String strFilePath) {
        int endIndex = strFilePath.lastIndexOf("/");
        if (endIndex == -1) {
            endIndex = strFilePath.lastIndexOf("\\");
        }
        String path = strFilePath.substring(0, endIndex);
        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(strFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将dir目录下多有文件内容整合成一个文件
     *
     * @param dir
     * @param out
     * @param filter 过滤器
     * @return
     */
    public static File unite(Path dir, Path out, FileFilter filter) {
        try (final FileChannel channel = FileChannel.open(
                out, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            Files.walk(dir, FileVisitOption.FOLLOW_LINKS)
                    .filter(path -> filter.accept(path.toFile()))
                    .map(path -> {
                        System.out.println(path);
                        try {
                            return FileChannel.open(path, StandardOpenOption.READ);
                        } catch (IOException e) {
                            System.out.println(path);
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .forEach(channel1 -> {
                        append(channel1, channel);
                        try {
                            channel1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toFile();
    }

    private static void append(FileChannel in, FileChannel out) {
        try {
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开写文件的BufferedWriter输出流
     *
     * @param file     目标文件
     * @param isAppend 是否以追加方式打开
     * @param encoding 编码
     * @return
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static BufferedWriter openFileBufferedWriterStream(File file, boolean isAppend, String encoding) {
        try {
            return new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, isAppend), encoding));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件复制
     *
     * @param source 源文件绝对路径
     * @param dest   目标文件绝对路径
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void copyTo(String source, String dest) {
        File sourceFile = new File(source);
        File destFile = new File(dest);
        copyTo(sourceFile, destFile);
    }

    /**
     * 文件复制
     *
     * @param in  源文件
     * @param out 目标文件
     */
    public static void copyTo(File in, File out) {
        try {
            if (!out.exists()) {
                assert out.createNewFile();
            }
            FileInputStream fIn = new FileInputStream(in);
            FileOutputStream fOut = new FileOutputStream(out);
            FileChannel fcIn = fIn.getChannel();
            FileChannel fcOut = fOut.getChannel();
            fcIn.transferTo(0, fcIn.size(), fcOut);
            fIn.close();
            fcIn.close();
            fOut.close();
            fcOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean rename(String file1, String file2) {
        File f1 = new File(file1);
        return f1.renameTo(new File(file2));
    }

    public static void delEmptyDir(String path) {
        File root = new File(path);
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            File[] subFiles = file.listFiles();
            if (subFiles == null || subFiles.length == 0) {
                file.delete();
                System.out.println(file.getAbsolutePath() + "--deleted.");
            }
        }
    }

    public static void main(String[] args) {
        unite(Paths.get("d:", "test", "dialup", "kafka"),
                Paths.get("d:", "test", "dialup", "2016-09-22_kafka.log"),
                file -> file.toString().endsWith(".log"));
    }
}
