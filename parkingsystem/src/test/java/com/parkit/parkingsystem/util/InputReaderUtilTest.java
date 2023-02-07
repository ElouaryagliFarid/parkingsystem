package com.parkit.parkingsystem.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.altindag.log.LogCaptor;

public class InputReaderUtilTest {

	private InputReaderUtil inputReaderUtil;
	private static LogCaptor logCaptor;

	@BeforeAll
	static void setUp() {

	}

	@BeforeEach
	private void setUpPerTest() {

		logCaptor = LogCaptor.forName("InputReaderUtil");
		logCaptor.setLogLevelToInfo();

	}

	@Test
	@DisplayName("lire le test de sélection avec une entrée valide")
	void readSelectionTest_withValidInput() throws IOException {

		// Given
		System.setIn(System.in);
		String in = "1";

		ByteArrayInputStream stream = new ByteArrayInputStream(
				in.getBytes(StandardCharsets.UTF_8));
		System.setIn(stream);
		inputReaderUtil = new InputReaderUtil();

		// then
		assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
		System.setIn(System.in);
		stream.close();
	}

	@Test
	@DisplayName("Lire le test de sélection avec une entrée valide")
	void readSelectionTest_WithInvalidInput() throws IOException {
		// Given
		System.setIn(System.in);
		String input = "m";
		ByteArrayInputStream stream = new ByteArrayInputStream(
				input.getBytes());
		System.setIn(stream);
		inputReaderUtil = new InputReaderUtil();
		// then
		assertThat(inputReaderUtil.readSelection()).isEqualTo(-1);
		stream.close();
	}

}
