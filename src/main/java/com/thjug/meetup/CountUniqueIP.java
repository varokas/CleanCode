/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thjug.meetup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 *
 * @author nuboat
 */
class Result {

	public final String key;
	public final Integer value;

	public Result(final String key, final Integer value) {
		this.key = key;
		this.value = value;
	}
}

public class CountUniqueIP {

	private static final String PATH = "./src/main/resources/";
	private static final String NAME = "crm.in.th.log.1";
	private static final String RESULT = "result.txt";

	public static void main(final String[] args) {
        run();
	}

    public static void run() {
        List<String> lines = readDataToBuffer();
        List<Result> mapResult = mapDatabyIP(lines);
        Map<String, List<Integer>> groupValue = groupbyIP(mapResult);
        Map<String, Integer> ipmap = reduceValue(groupValue);
        writeoutputtofile(ipmap);
    }

    private static void writeoutputtofile(final Map<String, Integer> ipmap) {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(
                    FileSystems.getDefault().getPath(PATH, RESULT),
                    Charset.defaultCharset(),
                    StandardOpenOption.CREATE);

            for (final String ip : ipmap.keySet()) {
                writer.write(ip + " " + ipmap.get(ip) + "\n");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<String, Integer> reduceValue(final Map<String, List<Integer>> groupValue) {
        final Map<String, Integer> ipmap = new HashMap<>();

        for (final String key : groupValue.keySet()) {
            final List<Integer> list = groupValue.get(key);

            Integer total = 0;
            for (final Integer i : list) {
                total += i;
            }

            ipmap.put(key, total);
        }

        return ipmap;
    }

    private static Map<String, List<Integer>> groupbyIP(final List<Result> mapResult) {
        final Map<String, List<Integer>> groupValue = new HashMap<>();

        for (final Result p : mapResult) {
            final List<Integer> list = groupValue.getOrDefault(p.key, new LinkedList<Integer>());
            list.add(p.value);

            groupValue.put(p.key, list);
        }

        return groupValue;
    }

    private static List<Result> mapDatabyIP(final List<String> lines) {
        final List<Result> mapResult = new LinkedList<>();

        for (final String log : lines) {
            final String ip = log.split(" - - ")[0];
            mapResult.add(new Result(ip, 1));
        }

        return mapResult;
    }

    private static List<String> readDataToBuffer() {
        final List<String> lines = new LinkedList<>();
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(
                    FileSystems.getDefault().getPath(PATH, NAME),
                    Charset.defaultCharset());

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            System.out.println("count: " + lines.size());

        } catch (final IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lines;
    }

}
