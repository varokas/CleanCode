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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

		final List<String> lines = new LinkedList<>();
		final List<Result> mapResult = new LinkedList<>();
		final Map<String, List<Integer>> groupValue = new HashMap<>();
		final Map<String, Integer> ipmap = new HashMap<>();

		// Read data to buffer.
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
		// End Read data to buffer.

		// Map Data by IP
		for (final String log : lines) {
			final String ip = log.split(" - - ")[0];
			mapResult.add(new Result(ip, 1));
		}
		// End Map Data by IP.

		// Group by IP
		for (final Result p : mapResult) {
			final List<Integer> list = groupValue.getOrDefault(p.key, new LinkedList<Integer>());
			list.add(p.value);

			groupValue.put(p.key, list);
		}
		// End Group by IP

		// Reduce Value
		for (final String key : groupValue.keySet()) {
			final List<Integer> list = groupValue.get(key);

			Integer total = 0;
			for (final Integer i : list) {
				total += i;
			}

			ipmap.put(key, total);
		}
		// End Reduce value

		// Write output to file
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
		// End Write output to file
	}

}
