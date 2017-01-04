package com.ge.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Storm_Falcon on 2016/6/17.
 * Read file as line one by one.
 */
@SuppressWarnings("unused")
public class FileReader implements Closeable {

    private BufferedReader mReader = null;

    private int mLineNum = 0;

    private String strCurrentLine = null;

    private static final BiFunction<Integer, String, String> DEFAULT_PROCESSOR = (lineNum, line) -> line;

    public boolean open(String strFilePath) {
        return open(strFilePath, "GBK");
    }

    public boolean open(String strFilePath, String encoding) {
        mLineNum = 0;
        File file = new File(strFilePath);
        if (!file.exists()) {
            System.out.println("File not found:" + strFilePath);
            return false;
        }

        try {
            mReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getLine() {
        return strCurrentLine;
    }

    public String nextLine() {
        if (hasNext()) {
            return strCurrentLine;
        }
        return null;
    }

    public boolean hasNext() {
        try {
            mLineNum++;
            return (strCurrentLine = mReader != null ? mReader.readLine() : null) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getLineNumber() {
        return mLineNum;
    }

    public void close() {
        mLineNum = 0;
        try {
            if (mReader != null) {
                mReader.close();
                mReader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method can iterate file's content as line.
     * And also can map the content and the line number to another value.
     *
     * @param filePath The file's absolute path.
     * @return A stream which is consists of the file content.
     * @see #forEachWithProcessor(String, String, BiFunction)
     */
    public static Stream<String> mapForEach(String filePath) {
        return forEachWithProcessor(filePath, "gbk", DEFAULT_PROCESSOR);
    }

    /**
     * This method can iterate file's content as line then return a {@code java.util.stream.Stream}
     * which is consists of {@code T}.
     * And also can map the content and the line number to another value.
     *
     * @param filePath  The file's absolute path.
     * @param processor A {@code java.util.function.BiFunction} which defines how to map.
     * @param <T>       Map result.
     * @return A stream which is consists of {@code T}.
     * @see #forEachWithProcessor(String, String, BiFunction)
     */
    public static <T> Stream<T> mapForEach(String filePath, BiFunction<Integer, String, T> processor) {
        return forEachWithProcessor(filePath, "gbk", processor);
    }

    /**
     * This method can iterate file's content as line then return a {@code java.util.stream.Stream}
     * which is consists of {@code T}.
     * And also can map the content and the line number to another value.
     *
     * @param filePath  The file's absolute path.
     * @param encode    The file's encode. Default encode is GBK.
     * @param processor A {@code java.util.function.BiFunction} which defines how to map.
     * @param <T>       Map result.
     * @return A stream which is consists of {@code T}.
     * @see #forEachWithProcessor(String, String, BiFunction)
     */
    public static <T> Stream<T> mapForEach(String filePath, String encode, BiFunction<Integer, String, T> processor) {
        return forEachWithProcessor(filePath, encode, processor);
    }

    /**
     * This method can iterate file's content as line then return a {@code java.util.stream.Stream}
     * which is consists of {@code T}.
     * And also can map the content and the line number to another value.<br>
     * <p>
     * Example:To calculate a file's total characters.<br>
     * <pre>{@code
     *     FileReader.mapForEach(filePath, (lineNumber, lineContent) -> lineContent.length())
     *     		.mapToInt(length -> length)
     *     		.sum();
     *     }</pre>
     *
     * @param filePath  The file's absolute path.
     * @param encode    The file's encode. Default encode is GBK.
     * @param processor A {@code java.util.function.BiFunction} which defines how to map.
     * @param <T>       Map result.
     * @return A stream which is consists of {@code T}.
     */
    private static <T> Stream<T> forEachWithProcessor(String filePath, String encode, BiFunction<Integer, String, T> processor) {
        try (FileReader reader = new FileReader()) {
            reader.open(filePath, encode);
            Stream<T> stream = Stream.empty();

            while (reader.hasNext()) {
                int lineNumber = reader.getLineNumber();
                String line = reader.getLine();
                T t = processor.apply(lineNumber, line);
                stream = Stream.concat(stream, Stream.of(t));
            }

            return stream;
        }
    }

    /**
     * This method can iterate a file content as line without return value.
     *
     * @param filePath The file's absolute path.
     * @param consumer Defined what to do with a file content line.
     * @see #forEachWithConsumer(String, String, Consumer)
     */
    public static void forEach(String filePath, Consumer<String> consumer) {
        forEachWithConsumer(filePath, "GBK", consumer);
    }

    /**
     * This method can iterate a file content as line without return value.
     *
     * @param filePath The file's absolute path.
     * @param encode   The file's encode. Default encode is GBK.
     * @param consumer Defined what to do with a file content line.
     * @see #forEachWithConsumer(String, String, Consumer)
     */
    public static void forEach(String filePath, String encode, Consumer<String> consumer) {
        forEachWithConsumer(filePath, encode, consumer);
    }

    /**
     * This method can iterate a file content as line without return value.<br>
     * Example:Print out the file content as line.
     * <pre>{@code
     * 	FileReader.forEach("D:/a.txt", System.out::println);
     * }</pre>
     *
     * @param filePath The file's absolute path.
     * @param encode   The file's encode. Default encode is GBK.
     * @param consumer Defined what to do with a file content line.
     */
    private static void forEachWithConsumer(String filePath, String encode, Consumer<String> consumer) {
        try {
            Files.lines(Paths.get(filePath), Charset.forName(encode)).forEach(consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "E:\\Document\\Big Data\\test_sort_data.txt";
        FileReader.mapForEach(filePath, (num, e) -> {
            if (num > 10) {
                return null;
            }
            return e;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList())
                .forEach(System.out::println);

        FileReader.forEach(filePath, System.out::println);

        long sum = FileReader.mapForEach(
                filePath,
                (lineNumber, lineContent) -> lineContent.length()
        ).mapToInt(n -> n)
                .sum();
        System.out.println(sum);
    }
}
