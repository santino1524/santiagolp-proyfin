package com.ntd.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import lombok.extern.slf4j.Slf4j;

/**
 * Generar Codigo QR
 * 
 * @author SLP
 */
@Slf4j
public class QRCode {

	/** Constructor privado */
	private QRCode() {
	}

	/**
	 * Generar QR
	 * 
	 * @param text
	 * @param width
	 * @param height
	 * @return Image
	 * @throws WriterException
	 * @throws IOException
	 * @throws BadElementException
	 */
	public static Image generateQRCodeImage(String text, int width, int height)
			throws WriterException, IOException, BadElementException {
		if (log.isInfoEnabled())
			log.info("Generar QR");

		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
		byte[] qrCodeBytes = out.toByteArray();

		return Image.getInstance(qrCodeBytes);
	}
}
