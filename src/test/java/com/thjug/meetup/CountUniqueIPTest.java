package com.thjug.meetup;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CountUniqueIPTest {
    @Test
    public void countUniqueIP() throws Exception {
        CountUniqueIP.run();

        String expected = readFile("./src/test/resources/expectedResult.txt");
        String actual = readFile("./src/main/resources/result.txt");

        assertThat(actual, equalTo(expected));
    }

    private String readFile(final String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
