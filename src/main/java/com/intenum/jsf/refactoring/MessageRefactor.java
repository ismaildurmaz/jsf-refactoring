package com.intenum.jsf.refactoring;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.intenum.common.Tracer;

public class MessageRefactor {
	private String messageTag;
	private File propertyFile;

	public String getMessageTag() {
		return messageTag;
	}

	public void setMessageTag(String messageTag) {
		this.messageTag = messageTag;
	}

	public List<File> listHTMLFiles(File webAppDirectory) {
		return (List<File>) FileUtils.listFiles(webAppDirectory, new String[] {
				"xhtml", "html", "jsp", "jsf" }, true);
	}

	public File getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(File propertyFile) {
		this.propertyFile = propertyFile;
	}

	public void updatePropertyFile(String oldValue, String newValue)
			throws Exception {
		if (StringUtils.isEmpty(messageTag)) {
			throw new Exception("Message tag cannot be empty");
		}
		if (propertyFile == null) {
			throw new Exception("Property file cannot be empty");
		}
		String content = FileUtils.readFileToString(propertyFile, "UTF-8");
		String patternText = String.format("%s\\s*\\u003d", oldValue);
		String replaceText = String.format("%s =", newValue);
		Pattern pattern = Pattern.compile(patternText,
				Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(content);
		content = matcher.replaceFirst(replaceText);
		FileUtils.writeStringToFile(propertyFile, content);
	}

	public void executeInFolder(File folder, String oldValue, String newValue,
			boolean updateProperty) throws Exception {
		if (updateProperty) {
			updatePropertyFile(oldValue, newValue);
		}
		List<File> list = listHTMLFiles(folder);
		for (File file : list) {
			execute(file, oldValue, newValue);
		}
	}

	public void execute(File file, String oldValue, String newValue)
			throws Exception {
		if (StringUtils.isEmpty(messageTag)) {
			throw new Exception("Message tag cannot be empty");
		}
		String content = FileUtils.readFileToString(file, "UTF-8");

		// #{msg.test} or #{msg['test']}
		String patternText = String
				.format("(\\u0023\\u007b%s\\.%s\\u007d)|(\\u0023\\u007b%s\\u005b\\u0027%s\\u0027\\u005d\\u007d)",
						messageTag, oldValue, messageTag, oldValue);

		String replaceText = String.format("#{%s['%s']}", messageTag, newValue);
		Pattern pattern = Pattern.compile(patternText,
				Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			content = matcher.replaceAll(replaceText);
			FileUtils.writeStringToFile(file, content);
		}
	}
}
