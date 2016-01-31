package com.hyperiongray.rcmp.extract;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyperiongray.rcmp.ExtractedData;

public class Type1Extractor {

	public ExtractedData extractData(List<String> tokens, String pdfText) throws IOException {
		Map<DataKey, String> ret = new HashMap<DataKey, String>();
		BetweenMarkerExtractor extractor = new BetweenMarkerExtractor();
		
		String involvedBlockRegex = "Involved .+?:";
		
		ret.put(DataKey.REPORT_NO, extractor.extract("Report no.:", "Occurrence Type:", pdfText));
		ret.put(DataKey.OCCURRENCE_TYPE, extractor.extract("Occurrence Type:", "Occurrence time:", pdfText));
		ret.put(DataKey.OCCURRENCE_TIME, extractor.extract("Occurrence time:", "Reported time:", pdfText));
		ret.put(DataKey.REPORTED_TIME, extractor.extract("Reported time:", "Place of offence:", pdfText));
		ret.put(DataKey.PLACE_OF_OFFENCE, extractor.extract("Place of offence:", "Clearance status:", pdfText));
		ret.put(DataKey.CLEARANCE_STATUS, extractor.extract("Clearance status:", "Concluded:", pdfText));
		ret.put(DataKey.CONCLUDED, extractor.extract("Concluded:", "Concluded date:", pdfText));
		ret.put(DataKey.CONCLUDED_DATE, extractor.extract("Concluded date:", "Summary:", pdfText));
		ret.put(DataKey.SUMMARY, extractor.extract("Summary:", "Remarks:", pdfText));
		ret.put(DataKey.REMARKS, extractor.extract("Remarks:", "Associated occurrences:", pdfText));
		ret.put(DataKey.ASSOCIATED_OCCURRENCES, extractor.extract("Associated occurrences:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_PERSONS, extractor.extract("Involved persons:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_ADDRESSES, extractor.extract("Involved addresses:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_COMM_ADDRESSES, extractor.extract("Involved comm addresses:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_VEHICLES, extractor.extract("Involved vehicles:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_OFFICERS, extractor.extract("Involved officers:", involvedBlockRegex, pdfText));
		ret.put(DataKey.INVOLVED_PROPERTY, extractor.extract("Involved property:", "Modus operandi:", pdfText));
		ret.put(DataKey.MODUS_OPERANDI, extractor.extract("Modus operandi:", "Reports:", pdfText));
		ret.put(DataKey.REPORTS, extractor.extract("Reports:", "Supplementary report:", pdfText));
		ret.put(DataKey.SUPPLEMENTARY_REPORT, extractor.extract("Supplementary report:", "", pdfText));
		
//		// some cleanup
		ret.put(DataKey.OCCURRENCE_TIME, sanitizeOccurrenceTime(ret.get(DataKey.OCCURRENCE_TIME)));
		ret.put(DataKey.INVOLVED_PERSONS, sanitizeBlock(ret.get(DataKey.INVOLVED_PERSONS)));
		ret.put(DataKey.INVOLVED_OFFICERS, sanitizeBlock(ret.get(DataKey.INVOLVED_OFFICERS)));
		ret.put(DataKey.INVOLVED_ADDRESSES, sanitizeBlock(ret.get(DataKey.INVOLVED_ADDRESSES)));
		ret.put(DataKey.INVOLVED_VEHICLES, sanitizeBlock(ret.get(DataKey.INVOLVED_VEHICLES)));
		
		return new ExtractedData(1, ret);
	}

	private String sanitizeBlock(String text) {
		if (text == null) {
			return "";
		}
		if (text.startsWith("z ")) {
			return text.substring(2);
		}
		return text;
	}

	private String sanitizeOccurrenceTime(String text) {
		if (text == null) {
			return "";
		}
		if (text.endsWith("-")) {
			text = text.substring(0, text.length() - 1);
		}
		return text.trim();
	}
	
}
