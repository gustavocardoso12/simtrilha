package org.applicationn.pesquisa.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.applicationn.pesquisa.service.TbPesquisaService;
import org.applicationn.pesquisa.vo.EmpresasDetalheVO;
import org.applicationn.pesquisa.vo.GradeVO;
import org.applicationn.pesquisa.vo.MediasNovaEmpresaVO;
import org.applicationn.pesquisa.vo.MediasVO;
import org.applicationn.simtrilhas.domain.security.UserEntity;
import org.applicationn.simtrilhas.web.util.MessageFactory;


public class Exportar {


	public static String formataCargo (String cargoEscolhido) {
		String[] palavras = cargoEscolhido.toLowerCase().split(" ");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < palavras.length; i++) {
			if (i > 0 && (palavras[i].equals("de") || palavras[i].equals("da"))) {
				sb.append(palavras[i]).append(" ");
			} else {
				sb.append(palavras[i].substring(0, 1).toUpperCase())
				.append(palavras[i].substring(1)).append(" ");
			}
		}
		String cargoFormatado = sb.toString().trim();
		//System.out.println(cargoFormatado);
		return cargoFormatado;
	}

	private static void removeGridlines(Sheet sheet) {
		sheet.setDisplayGridlines(false);

	}

	private static void addImage(Sheet sheet, String imagePath) throws IOException {
		Row row0 = sheet.createRow(0);
		Cell cell0 = row0.createCell(0);
		BufferedImage image = ImageIO.read(new File(imagePath));
		int imageWidth = image.getWidth();
		int imageHeight = (int) (image.getHeight());
		sheet.getRow(0).setHeight((short) (imageHeight * 7));

		byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
		int pictureIndex = sheet.getWorkbook().addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
		Drawing drawing = sheet.createDrawingPatriarch();
		//	ClientAnchor anchor = drawing.createAnchor(0, 0, imageWidth, imageHeight, 0, 0, 3, 1);

		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 1, 1);
		anchor.setCol1(0); // Posiciona a imagem na primeira coluna
		anchor.setRow1(0); // Posiciona a imagem na primeira linha
		anchor.setCol2(3); // Larga a imagem na quarta coluna
		anchor.setRow2(2); // Solta a imagem na sexta linha
		anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE); 
		Picture picture = drawing.createPicture(anchor, pictureIndex);
		picture.resize(0.63, 1);
	}




	private static Row createRows(Sheet sheet, int numerodaLinha, short altura) {
		Row row = sheet.createRow(numerodaLinha);
		row.setHeight(altura);
		return row;
	}

	private static Cell createCells(Sheet sheet, int numeroDaCelula, String valorDaCelula, Row row) {
		Cell cell = row.createCell(numeroDaCelula);
		cell.setCellValue(valorDaCelula);
		return cell;
	}


	private static Cell createCellsQuebra(Sheet sheet, int numeroDaCelula, XSSFRichTextString  valorDaCelula, Row row) {
		Cell cell = row.createCell(numeroDaCelula);
		cell.setCellValue(valorDaCelula);
		return cell;
	}

	private static void addMergeRegion(Sheet sheet, CellRangeAddress rangeTotal) {
		CellRangeAddress mergedRegion = rangeTotal;
		sheet.addMergedRegion(mergedRegion);
	}


	private static void createStyle (Sheet sheet, VerticalAlignment alinhamentoVertical,
			HorizontalAlignment alinhamentoHorizontal,Font fonte, Cell celula ) {
		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setVerticalAlignment(alinhamentoVertical);
		style.setAlignment(alinhamentoHorizontal);
		style.setFont(fonte);
		celula.setCellStyle(style);
		//sheet.autoSizeColumn(celula.getColumnIndex());
	}



	private static Font createFont (Sheet sheet, String name, short HeightOnPoints, boolean bold, short color) {
		Font font= sheet.getWorkbook().createFont();
		font.setFontName(name);
		font.setFontHeightInPoints(HeightOnPoints);
		font.setBold(bold);
		if (color == 0) {

		}else {
			font.setColor(color);
		}
		return font;
	}

	private static XSSFCellStyle createXSSFStyle (Sheet sheet,Workbook workbook, VerticalAlignment alinhamentoVertical,
			HorizontalAlignment alinhamentoHorizontal,Font fonte, XSSFColor color, FillPatternType padrao,
			int quebra ) {
		XSSFCellStyle  style =  (XSSFCellStyle) workbook.createCellStyle();
		style.setVerticalAlignment(alinhamentoVertical);
		style.setAlignment(alinhamentoHorizontal);
		style.setFont(fonte);
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.WHITE.getIndex());
		style.setFillForegroundColor(color);
		style.setFillPattern(padrao);

		if(quebra==1) {

			style.setWrapText(true);
		}

		return style;
	}


	private static XSSFCellStyle createXSSFStyleBordasEscuras (Sheet sheet,Workbook workbook, VerticalAlignment alinhamentoVertical,
			HorizontalAlignment alinhamentoHorizontal,Font fonte, XSSFColor color, FillPatternType padrao,
			int quebra ) {
		XSSFCellStyle  style =  (XSSFCellStyle) workbook.createCellStyle();
		style.setVerticalAlignment(alinhamentoVertical);
		style.setAlignment(alinhamentoHorizontal);
		style.setFont(fonte);
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setFillForegroundColor(color);
		style.setFillPattern(padrao);

		if(quebra==1) {

			style.setWrapText(true);
		}

		return style;
	}


	public static XSSFColor rgbToXSSFColor(int red, float green, float blue) {
		// Converter a cor RGB para valores de ponto flutuante de 0 a 1
		float r = red / 255f;
		float g = green / 255f;
		float b = blue / 255f;

		// Multiplicar os valores em ponto flutuante pelo valor máximo de cor (255)
		int rValue = Math.round(r * 255);
		int gValue = Math.round(g * 255);
		int bValue = Math.round(b * 255);

		// Criar um objeto XSSFColor com os valores RGB
		return new XSSFColor(new byte[] { (byte) rValue, (byte) gValue, (byte) bValue }, new DefaultIndexedColorMap());
	}

	private static void addHeader(Sheet sheet, String cargoEscolhido, String nomeEmpresa, Workbook workbook,int ExportOption,
			String Subfamilia) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(new Date());

		// criar a linha 4
		Row row4 = createRows(sheet, 4, (short) 600);
		Cell cellA5 =null;
		// criar a primeira linha
		if(ExportOption==2) {
			cellA5 = createCells(sheet, 0, Subfamilia + " X Mercado", row4);
		}else {
			cellA5 = createCells(sheet, 0, cargoEscolhido + " X Mercado", row4);
		}

		addMergeRegion(sheet, new CellRangeAddress(4, 4, 0, 28));
		Font fontA5 = createFont(sheet, "Helvetica", (short) 12, true, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA5, cellA5);

		// criar a segunda linha
		Row row5 = createRows(sheet, 5, (short) 600);
		Cell cellA6 = createCells(sheet, 0, "" + nomeEmpresa + ", " + formattedDate, row5);
		Font fontA6 = createFont(sheet, "Calibri", (short) 11, false, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA6, cellA6);




		// criar a oitava linha
		Row row8 = createRows(sheet, 8, (short) 600);

		Cell cellD9 = createCells(sheet, 4, "SBM - Salário Base Mensal", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 4, 7));
		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.WHITE.getIndex());
		XSSFColor color = rgbToXSSFColor(170,123,187);
		XSSFCellStyle style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10, 
				color, FillPatternType.SOLID_FOREGROUND,0);
		cellD9.setCellStyle(style);

		color =  rgbToXSSFColor(110,66,126);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,1);

		String textoApresentacao = "ICPA - Incentivo de Curto Prazo Alvo\n"
				+ " Bônus + PLR - Em múltiplos do Salário Base";
		XSSFRichTextString content = new XSSFRichTextString(textoApresentacao);
		int quebraLinhaIndex = content.getString().indexOf('\n');
		XSSFFont italicFont = (XSSFFont) workbook.createFont();
		italicFont.setItalic(true);
		color =  rgbToXSSFColor(255,255,255);
		italicFont.setFontName("Helvetica");
		italicFont.setColor(color);
		content.applyFont(quebraLinhaIndex + 1, content.length(), italicFont);

		Cell cellD20 = createCellsQuebra(sheet, 8, content, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 8, 11));
		cellD20.setCellStyle(style);


		String textoApresentacaoICP = "ICP - Incentivo de Curto Prazo Pago \n"
				+ " Bônus + PLR - Em múltiplos do Salário Base";
		XSSFRichTextString contentICP = new XSSFRichTextString(textoApresentacaoICP);
		int quebraLinhaIndexICP = contentICP.getString().indexOf('\n');
		XSSFFont italicFontICP = (XSSFFont) workbook.createFont();
		italicFontICP.setItalic(true);
		color =  rgbToXSSFColor(255,255,255);
		italicFontICP.setFontName("Helvetica");
		italicFontICP.setColor(color);
		contentICP.applyFont(quebraLinhaIndexICP + 1, contentICP.length(), italicFontICP);

		Cell cellD21 = createCellsQuebra(sheet, 12, contentICP, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 12, 15));
		cellD21.setCellStyle(style);


		color =  rgbToXSSFColor(255,82,80);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10, 
				color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD22 = createCells(sheet, 16, "TDA - Total em Dinheiro Alvo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 16, 19));
		cellD22.setCellStyle(style);




		color =  rgbToXSSFColor(233,77,101);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD23 = createCells(sheet, 20, "TD - Total em Dinheiro", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 20, 23));
		cellD23.setCellStyle(style);



		color =  rgbToXSSFColor(128,128,128);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD24 = createCells(sheet, 24, "RDA - Remuneração Direta Alvo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 24, 27));
		cellD24.setCellStyle(style);




		color =  rgbToXSSFColor(89,89,89);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD25 = createCells(sheet, 28, "RD - Remuneração Direta", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 28, 31));
		cellD25.setCellStyle(style);


		color =  rgbToXSSFColor(83, 186, 69);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD26 = createCells(sheet, 32, "ILP - Incentivo de Longo Prazo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 32, 35));
		cellD26.setCellStyle(style);



		// criar a nona linha (PRIMEIRO HEADER)
		Row row9 = createRows(sheet, 9, (short) 400);
		String[] headerTitles = { "Família", "Subfamilia", "Cargo AptaXR", "Cargo Empresa", "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)"};
		color =  rgbToXSSFColor(226,206,228);
		Font fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
		for (int i = 0; i < headerTitles.length; i++) {
			Cell cell = createCells(sheet, i, headerTitles[i], row9);

			if (i < 3) {
				sheet.setAutoFilter(new CellRangeAddress(9, sheet.getLastRowNum(), 0, i));
			}

			if((headerTitles[i]=="Sua empresa") || headerTitles[i]=="Família" 
					|| headerTitles[i]=="Subfamilia" || headerTitles[i]=="Cargo"   || headerTitles[i]=="Cargo Empresa"	 	) {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha segundo header (SBA)
		String[] headerSBA = { "Sua empresa", "Percentil 25 (P25)",
				"Percen"
						+ "til 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerSBA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length, headerSBA[i], row9);

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (ICPA)
		String[] headerICPA = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerICPA.length; i++) {
			if(headerICPA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length, headerICPA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TDA)
		String[] headerTDA = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerTDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length, headerTDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TD)
		String[] headerTD = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerTD.length; i++) {
			if(headerTD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length, headerTD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RDA)
		String[] headerRDA = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerRDA.length; i++) {
			if(headerRDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length, headerRDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RD)
		String[] headerRD = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerRD.length; i++) {
			if(headerRD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length, headerRD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}



		//nona linha quarto header (ILP)


		String[] headerILP = { "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)" };

		for (int i = 0; i < headerILP.length; i++) {
			if(headerILP[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length +  headerRD.length, headerILP[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

	}

	public static String capitalizeWords(String str) {
		String[] words = str.toLowerCase().split("\\s+");
		StringBuilder capitalized = new StringBuilder();

		for (String word : words) {
			if (word.length() > 0) {
				capitalized.append(Character.toUpperCase(word.charAt(0)))
				.append(word.substring(1)).append(" ");
			}
		}
		return capitalized.toString().trim();
	}

	private static void addHeaderNew(Sheet sheet, String cargoEscolhido, String nomeEmpresa, Workbook workbook,int ExportOption,
			String Subfamilia) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(new Date());

		// criar a linha 4
		Row row4 = createRows(sheet, 4, (short) 600);
		Cell cellA5 =null;
		// criar a primeira linha

		String descRel = "";
		if(ExportOption==3) {
			descRel="Relatório Geral da Empresa: " + capitalizeWords(cargoEscolhido);
		}
		if(ExportOption==4) {
			descRel="Relatório Geral do Mercado: ";
		}

		cellA5 = createCells(sheet, 1, descRel   , row4);


		addMergeRegion(sheet, new CellRangeAddress(4, 4, 1, 28));
		Font fontA5 = createFont(sheet, "Helvetica", (short) 12, true, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA5, cellA5);

		// criar a segunda linha
		Row row5 = createRows(sheet, 5, (short) 600);
		Cell cellA6 = createCells(sheet, 1, formattedDate, row5);
		Font fontA6 = createFont(sheet, "Calibri", (short) 11, false, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA6, cellA6);



		// criar a oitava linha
		Row row8 = createRows(sheet, 8, (short) 600);

		Cell cellI9 = createCells(sheet, 9, "SBA - Salário Base Anual\n"
				+ "Salário + Adicionais mensais", row8); // Coluna alterada de 8 para 9
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 9, 14)); // 9-14
		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.WHITE.getIndex());
		XSSFColor color = rgbToXSSFColor(170, 123, 187);
		XSSFCellStyle style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10, 
				color, FillPatternType.SOLID_FOREGROUND, 1);
		cellI9.setCellStyle(style);

		color = rgbToXSSFColor(110, 66, 126);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);

		String textoApresentacao = "ICPA - Incentivo de Curto Prazo Alvo\n"
				+ " Bônus + PLR";
		XSSFRichTextString content = new XSSFRichTextString(textoApresentacao);
		int quebraLinhaIndex = content.getString().indexOf('\n');
		XSSFFont italicFont = (XSSFFont) workbook.createFont();
		italicFont.setItalic(true);
		color = rgbToXSSFColor(255, 255, 255);
		italicFont.setFontName("Helvetica");
		italicFont.setColor(color);
		content.applyFont(quebraLinhaIndex + 1, content.length(), italicFont);

		Cell cellI20 = createCellsQuebra(sheet, 15, content, row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 15, 20)); // 15-20
		cellI20.setCellStyle(style);

		String textoApresentacaoICP = "ICP - Incentivo de Curto Prazo Pago \n"
				+ " Bônus + PLR";
		XSSFRichTextString contentICP = new XSSFRichTextString(textoApresentacaoICP);
		int quebraLinhaIndexICP = contentICP.getString().indexOf('\n');
		XSSFFont italicFontICP = (XSSFFont) workbook.createFont();
		italicFontICP.setItalic(true);
		color = rgbToXSSFColor(255, 255, 255);
		italicFontICP.setFontName("Helvetica");
		italicFontICP.setColor(color);
		contentICP.applyFont(quebraLinhaIndexICP + 1, contentICP.length(), italicFontICP);

		Cell cellI21 = createCellsQuebra(sheet, 21, contentICP, row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 21, 26)); // 21-26
		cellI21.setCellStyle(style);

		color = rgbToXSSFColor(255, 82, 80);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10, 
				color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI22 = createCells(sheet, 27, "TDA - Total em Dinheiro Alvo\n"
				+ "Salário Base Anual + ICP Alvo", row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 27, 32)); // 27-32
		cellI22.setCellStyle(style);

		color = rgbToXSSFColor(233, 77, 101);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI23 = createCells(sheet, 33, "TD - Total em Dinheiro\n"
				+ "Salário Base Anual + ICP Pago", row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 33, 38)); // 33-38
		cellI23.setCellStyle(style);

		color = rgbToXSSFColor(83, 186, 69);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 0);
		Cell cellI24 = createCells(sheet, 39, "ILP - Incentivo de Longo Prazo", row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 39, 44)); // 39-44
		cellI24.setCellStyle(style);

		color = rgbToXSSFColor(128, 128, 128);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI25 = createCells(sheet, 45, "RDA - Remuneração Direta Alvo\n"
				+ "Total em Dinheiro Alvo + ILP", row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 45, 50)); // 45-50
		cellI25.setCellStyle(style);

		color = rgbToXSSFColor(89, 89, 89);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI26 = createCells(sheet, 51, "RD - Remuneração Direta\n"
				+ "Total em Dinheiro Pago + ILP", row8); // Ajuste para começar após a célula anterior
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 51, 56)); // 51-56
		cellI26.setCellStyle(style);

		// criar a nona linha (PRIMEIRO HEADER)
		Row row9 = createRows(sheet, 9, (short) 400);
		/*String[] headerTitles = { "Família", "Subfamilia", "Cargo Empresa", "Cargo APTAXR", "Matricula", "Grade Empresa", 
				"Código Cargo", "Grade XR", "Mercado", "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média", "Quantidade de Empresas"};*/


		String[] headerTitles = {
				"Mercado", // 1. Mercado
				"Familia", // 2. Família
				"Subfamilia", // 3. Subfamilia
				"Cargo APTAXR", // 4. Cargo APTAXR
				"Código Cargo", // 5. Código Cargo
				"Grade AptaXR", // 6. Grade XR (Grade AptaXR)
				"Matricula", // 7. Matricula
				"Cargo Empresa", // 8. Cargo Empresa
				"Grade Empresa", // 9. Grade Empresa
				"Sua empresa", // 10. Sua empresa
				"Quantidade de Empresas", // 11. Quantidade de Empresas
				"Percentil 25 (P25)", // 12. Percentil 25 (P25)
				"Percentil 50 (P50)", // 13. Percentil 50 (P50)
				"Percentil 75 (P75)", // 14. Percentil 75 (P75)
				"Média" // 15. Média
		};


		color =  rgbToXSSFColor(226,206,228);
		Font fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
		for (int i = 0; i < headerTitles.length; i++) {
			Cell cell = createCells(sheet, i, headerTitles[i], row9);

			if (i < 30) {
				sheet.setAutoFilter(new CellRangeAddress(9, sheet.getLastRowNum(), 0, i));
			}

			if((headerTitles[i]=="Sua empresa") || headerTitles[i]=="Família" 
					|| headerTitles[i]=="Subfamilia" || headerTitles[i]=="Cargo"   || headerTitles[i]=="Cargo Empresa"	 	) {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha segundo header (SBA)
		String[] headerSBA = { "Sua empresa", "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerSBA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length, headerSBA[i], row9);

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (ICPA)
		String[] headerICPA = { "Sua empresa", "Quantidade de empresas" , "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média"};

		for (int i = 0; i < headerICPA.length; i++) {
			if(headerICPA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length, headerICPA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TDA)
		String[] headerTDA = { "Sua empresa","Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerTDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length, headerTDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TD)
		String[] headerTD = { "Sua empresa", "Quantidade de empresas" , "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerTD.length; i++) {
			if(headerTD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length, headerTD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RDA)
		String[] headerRDA = { "Sua empresa", "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerRDA.length; i++) {
			if(headerRDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length, headerRDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RD)
		String[] headerRD = { "Sua empresa", "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerRD.length; i++) {
			if(headerRD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length, headerRD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}



		//nona linha quarto header (ILP)


		String[] headerILP = { "Sua empresa","Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerILP.length; i++) {
			if(headerILP[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length +  headerRD.length, headerILP[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

	}
	public static void applyAutoFilterToAllColumns(Sheet sheet) {
	    // Assume que a primeira linha contém os cabeçalhos e que queremos aplicar o filtro a todas as colunas usadas
	    int lastRow = sheet.getLastRowNum(); // Última linha com conteúdo
	    int lastColumn = sheet.getRow(0).getLastCellNum() - 1; // Última coluna com conteúdo (começando de 0)

	    // Define o intervalo para o autofiltro (da primeira coluna à última coluna, primeira linha à última linha)
	    String filterRange = "A1:" + (char) ('A' + lastColumn) + (lastRow + 1); // "A1:Z100" por exemplo
	    sheet.setAutoFilter(CellRangeAddress.valueOf(filterRange));
	}
	
	
	
	private static void addHeaderNewMerc(Sheet sheet, String cargoEscolhido, String nomeEmpresa, Workbook workbook,int ExportOption,
			String Subfamilia) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(new Date());

		// criar a linha 4
		Row row4 = createRows(sheet, 4, (short) 600);
		Cell cellA5 =null;
		// criar a primeira linha

		String descRel = "";
		if(ExportOption==3) {
			descRel="Relatório Geral da Empresa: " + capitalizeWords(cargoEscolhido);
		}
		if(ExportOption==4) {
			descRel="Relatório Geral do Mercado: ";
		}

		cellA5 = createCells(sheet, 1, descRel   , row4);


		addMergeRegion(sheet, new CellRangeAddress(4, 4, 1, 28));
		Font fontA5 = createFont(sheet, "Helvetica", (short) 12, true, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA5, cellA5);

		// criar a segunda linha
		Row row5 = createRows(sheet, 5, (short) 600);
		Cell cellA6 = createCells(sheet, 1, formattedDate, row5);
		Font fontA6 = createFont(sheet, "Calibri", (short) 11, false, (short) 0);
		createStyle(sheet, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA6, cellA6);



		// criar a oitava linha
		Row row8 = createRows(sheet, 8, (short) 600);

		Cell cellI9 = createCells(sheet, 5, "SBA - Salário Base Anual\n"
				+ "Salário + Adicionais mensais", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 5, 9)); // 9-14
		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.WHITE.getIndex());
		XSSFColor color = rgbToXSSFColor(170, 123, 187);
		XSSFCellStyle style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10,
				color, FillPatternType.SOLID_FOREGROUND, 1);
		cellI9.setCellStyle(style);

		color = rgbToXSSFColor(110, 66, 126);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);

		String textoApresentacao = "ICPA - Incentivo de Curto Prazo Alvo\n"
				+ " Bônus + PLR";
		XSSFRichTextString content = new XSSFRichTextString(textoApresentacao);
		int quebraLinhaIndex = content.getString().indexOf('\n');
		XSSFFont italicFont = (XSSFFont) workbook.createFont();
		italicFont.setItalic(true);
		color = rgbToXSSFColor(255, 255, 255);
		italicFont.setFontName("Helvetica");
		italicFont.setColor(color);
		content.applyFont(quebraLinhaIndex + 1, content.length(), italicFont);

		Cell cellI14 = createCellsQuebra(sheet, 10, content, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 10, 14)); // 10-14
		cellI14.setCellStyle(style);

		String textoApresentacaoICP = "ICP - Incentivo de Curto Prazo Pago \n"
				+ " Bônus + PLR";
		XSSFRichTextString contentICP = new XSSFRichTextString(textoApresentacaoICP);
		int quebraLinhaIndexICP = contentICP.getString().indexOf('\n');
		XSSFFont italicFontICP = (XSSFFont) workbook.createFont();
		italicFontICP.setItalic(true);
		color = rgbToXSSFColor(255, 255, 255);
		italicFontICP.setFontName("Helvetica");
		italicFontICP.setColor(color);
		contentICP.applyFont(quebraLinhaIndexICP + 1, contentICP.length(), italicFontICP);

		Cell cellI15 = createCellsQuebra(sheet, 15, contentICP, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 15, 19)); // 15-19
		cellI15.setCellStyle(style);

		color = rgbToXSSFColor(255, 82, 80);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10,
				color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI20 = createCells(sheet, 20, "TDA - Total em Dinheiro Alvo\n"
				+ "Salário Base Anual + ICP Alvo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 20, 24)); // 20-25
		cellI20.setCellStyle(style);

		color = rgbToXSSFColor(233, 77, 101);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI26 = createCells(sheet, 25, "TD - Total em Dinheiro\n"
				+ "Salário Base Anual + ICP Pago", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 25, 29)); // 26-31
		cellI26.setCellStyle(style);

		color = rgbToXSSFColor(83, 186, 69);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 0);
		Cell cellI32 = createCells(sheet, 30, "ILP - Incentivo de Longo Prazo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 30, 34)); // 32-37
		cellI32.setCellStyle(style);

		color = rgbToXSSFColor(128, 128, 128);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI38 = createCells(sheet, 35, "RDA - Remuneração Direta Alvo\n"
				+ "Total em Dinheiro Alvo + ILP", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 35, 39)); // 38-43
		cellI38.setCellStyle(style);

		color = rgbToXSSFColor(89, 89, 89);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND, 1);
		Cell cellI44 = createCells(sheet, 40, "RD - Remuneração Direta\n"
				+ "Total em Dinheiro Pago + ILP", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 40, 44)); // 44-49
		cellI44.setCellStyle(style);


		// criar a nona linha (PRIMEIRO HEADER)
		Row row9 = createRows(sheet, 9, (short) 400);
		/*String[] headerTitles = { "Família", "Subfamilia", "Cargo Empresa", "Cargo APTAXR", "Matricula", "Grade Empresa", 
				"Código Cargo", "Grade XR", "Mercado", "Sua empresa", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média", "Quantidade de Empresas"};*/


		String[] headerTitles = {
				"Mercado", // 1. Mercado
				"Familia", // 2. Família
				"Subfamilia", // 3. Subfamilia
				"Cargo APTAXR", // 4. Cargo APTAXR
				"Grade AptaXR", // 6. Grade XR (Grade AptaXR)
				"Quantidade de Empresas", // 11. Quantidade de Empresas
				"Percentil 25 (P25)", // 12. Percentil 25 (P25)
				"Percentil 50 (P50)", // 13. Percentil 50 (P50)
				"Percentil 75 (P75)", // 14. Percentil 75 (P75)
				"Média" // 15. Média
		};


		color =  rgbToXSSFColor(226,206,228);
		Font fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
		for (int i = 0; i < headerTitles.length; i++) {
			Cell cell = createCells(sheet, i, headerTitles[i], row9);

			if (i < 30) {
				sheet.setAutoFilter(new CellRangeAddress(9, sheet.getLastRowNum(), 0, i));
			}

			if((headerTitles[i]=="Sua empresa") || headerTitles[i]=="Família" 
					|| headerTitles[i]=="Subfamilia" || headerTitles[i]=="Cargo"   || headerTitles[i]=="Cargo Empresa"	 	) {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha segundo header (SBA)
		String[] headerSBA = { "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerSBA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length, headerSBA[i], row9);

			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (ICPA)
		String[] headerICPA = { "Quantidade de empresas" , "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média"};

		for (int i = 0; i < headerICPA.length; i++) {
			if(headerICPA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length, headerICPA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TDA)
		String[] headerTDA = { "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerSBA.length; i++) {
			if(headerTDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length, headerTDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}


		// nona linha terceiro header (TD)
		String[] headerTD = {"Quantidade de empresas" , "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerTD.length; i++) {
			if(headerTD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length, headerTD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RDA)
		String[] headerRDA = {"Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerRDA.length; i++) {
			if(headerRDA[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length, headerRDA[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

		// nona linha terceiro header (RD)
		String[] headerRD = { "Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerRD.length; i++) {
			if(headerRD[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length, headerRD[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}



		//nona linha quarto header (ILP)


		String[] headerILP = {"Quantidade de empresas", "Percentil 25 (P25)",
				"Percentil 50 (P50)", "Percentil 75 (P75)", "Média" };

		for (int i = 0; i < headerILP.length; i++) {
			if(headerILP[i]=="Sua empresa") {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, true,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}else {
				fontBlackheader = createFont(sheet, "Helvetica", (short) 12, false,
						IndexedColors.BLACK.getIndex());
				style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
						fontBlackheader, color, FillPatternType.SOLID_FOREGROUND,0);
			}
			Cell cell = createCells(sheet, i+headerTitles.length+ headerSBA.length 
					+headerICPA.length + headerTDA.length + headerTD.length
					+headerRDA.length +  headerRD.length, headerILP[i], row9);
			cell.setCellStyle(style);
			//sheet.autoSizeColumn(cell.getColumnIndex());
		}

	}

	public static String intToString(int valor) {
		return Integer.toString(valor);
	}


	public static String formatarMonetario (double valor) {
		NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String valorFormatado = formato.format(valor);
		if(valorFormatado==null) {
			valorFormatado="";
		}else if (valorFormatado.equals("R$ 0,00")) {
			valorFormatado =  "";
		}
		return valorFormatado;
	}

	public static String doubleToString(double number) {
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		String valor =  decimalFormat.format(number);
		valor = valor.replace(".", ",");
		if(valor==null) {
			valor="";
		}else if (valor.equals("0")) {
			valor = valor.replace("0", "");	
		}else if (valor.equals("0,0")) {
			valor = valor.replace("0.0", "");
		}

		return valor;
	}

	public static void addConteudoNew (List<MediasNovaEmpresaVO> dados, 
			Sheet sheet, Workbook workbook, Row row10,Map<String, Integer> descRenumMap 
			) {






		dados.removeIf(c -> c.getDescRenum().equals("SBM - Salário Base Mensal"));

		dados.sort(Comparator.comparing(c -> descRenumMap.get(c.getDescRenum())));


		for (Map.Entry<String, Integer> entry : descRenumMap.entrySet()) {
			String description = entry.getKey();
			boolean found = false;

			// Verificar se a descrição já existe em mediasVOList
			for (MediasNovaEmpresaVO mediasVO : dados) {
				if (mediasVO.getDescRenum().equals(description)) {
					found = true;
					break;
				}
			}

			// Se não encontrado, adicionar um novo objeto a mediasVOList
			if (!found) {
				MediasNovaEmpresaVO newMediasVO = new MediasNovaEmpresaVO();
				newMediasVO.setDescRenum(description);
				//newMediasVO.setDescricaoDoFiltro("");
				newMediasVO.setMedia(0);
				newMediasVO.setNomeCargo("");
				newMediasVO.setNumParticipantes(0);
				newMediasVO.setP10(0);
				newMediasVO.setP25(0);
				newMediasVO.setP50(0);
				newMediasVO.setP75(0);
				newMediasVO.setP90(0);
				newMediasVO.setQtdEmpresas(0);
				newMediasVO.setSuaEmpresa(0);
				//newMediasVO.setVersion(0);
				newMediasVO.setNomeCargoXr("");
				newMediasVO.setMatricula("");
				newMediasVO.setGradeEmpresa("");
				newMediasVO.setCodigoCargo("");
				dados.add(newMediasVO);
			}
		}

		int tamanhoAnterior = 0;
		String[] header= {};
		int posicaoSBM = 0;

		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
		XSSFColor color = new XSSFColor(new byte[]{(byte) 162, 50, 50}, new DefaultIndexedColorMap());
		XSSFCellStyle  style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER,
				HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);

		for(int k=0; k<dados.size();k++) {

			String descRenum = dados.get(k).getDescRenum();

			switch (descRenum) {
			case "SBA - Salário Base Anual":
				header = new String[] {
						dados.get(k).getMercado(), // Mercado
						dados.get(k).getNmFamilia(), // Família
						dados.get(k).getNmSubFamilia(), // Subfamilia
						dados.get(k).getNomeCargoXr(), // Cargo APTAXR
						dados.get(k).getCodigoCargo(), // Código Cargo
						String.valueOf(dados.get(k).getGrade()), // Grade XR (Grade AptaXR)
						dados.get(k).getMatricula(), // Matricula
						dados.get(k).getNomeCargo(), // Cargo Empresa
						dados.get(k).getGradeEmpresa(), // Grade Empresa
						formatarMonetario(dados.get(k).getSuaEmpresa()), // Sua empresa
						intToString(dados.get(k).getQtdEmpresas()), // Quantidade de Empresas
						formatarMonetario(dados.get(k).getP25()), // Percentil 25 (P25)
						formatarMonetario(dados.get(k).getP50()), // Percentil 50 (P50)
						formatarMonetario(dados.get(k).getP75()), // Percentil 75 (P75)
						formatarMonetario(dados.get(k).getMedia()) // Média
				};
				posicaoSBM = k;
				break;

			case "ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)":
				double v = dados.get(k).getSuaEmpresa();
				double v2 = dados.get(posicaoSBM).getSuaEmpresa();

				double suaEmpresa = (v2 != 0) ? v : 0;

				double p25 = dados.get(k).getP25();
				double p25v2 = dados.get(posicaoSBM).getP25();

				double p25Final = (p25v2 != 0) ? p25  : 0;

				double p50 = dados.get(k).getP50();
				double p50v2 = dados.get(posicaoSBM).getP50();

				double p50Final = (p50v2 != 0) ? p50 : 0;

				double p75 = dados.get(k).getP75();
				double p75v2 = dados.get(posicaoSBM).getP75();

				double p75Final = (p75v2 != 0) ? p75 : 0;

				header = new String[] { 
						formatarMonetario(suaEmpresa),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(p25Final),
						formatarMonetario(p50Final),
						formatarMonetario(p75Final),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;    

			case "ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)":
				double vICP = dados.get(k).getSuaEmpresa();
				double v2ICP = dados.get(posicaoSBM).getSuaEmpresa();

				double suaEmpresaICP = (v2ICP != 0) ? vICP  : 0;

				double p25ICP = dados.get(k).getP25();
				double p25v2ICP = dados.get(posicaoSBM).getP25();

				double p25FinalICP = (p25v2ICP != 0) ? p25ICP  : 0;

				double p50ICP = dados.get(k).getP50();
				double p50v2ICP = dados.get(posicaoSBM).getP50();

				double p50FinalICP = (p50v2ICP != 0) ? p50ICP : 0;

				double p75ICP = dados.get(k).getP75();
				double p75v2ICP = dados.get(posicaoSBM).getP75();

				double p75FinalICP = (p75v2ICP != 0) ? p75ICP  : 0;

				header = new String[] { 
						formatarMonetario(suaEmpresaICP),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(p25FinalICP),
						formatarMonetario(p50FinalICP),
						formatarMonetario(p75FinalICP),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "TD - Total em Dinheiro":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSuaEmpresa()),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "TDA - Total em Dinheiro Alvo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSuaEmpresa()),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "RDA - Remuneração Direta Alvo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSuaEmpresa()),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "RD - Remuneração Direta":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSuaEmpresa()),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "ILP - Incentivos de Longo Prazo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSuaEmpresa()),
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;
			}
			// Criação de fontes e cores
			Font fontA10Black = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
			Font fontA10BlackBold = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
			XSSFColor colorGray = rgbToXSSFColor(242, 242, 242);
			XSSFColor colorWhite = rgbToXSSFColor(255, 255, 255);

			// Criação dos estilos
			XSSFCellStyle styleGrayLeft = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA10Black, colorGray, FillPatternType.SOLID_FOREGROUND, 0);
			XSSFCellStyle styleGrayCenter = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10BlackBold, colorGray, FillPatternType.SOLID_FOREGROUND, 0);
			XSSFCellStyle styleWhiteCenter = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10Black, colorWhite, FillPatternType.SOLID_FOREGROUND, 0);

			for (int i = 0; i < header.length; i++) {
				if (header.length == 15) {
					if (i <= 8) {
						style = styleGrayLeft;
					} else {
						style = styleWhiteCenter;
					}
				} else {
					if (i == 0) {
						style = styleGrayCenter;
					} else {
						style = styleWhiteCenter;
					}
				}
				Cell cell = createCells(sheet, i + tamanhoAnterior, header[i], row10);
				cell.setCellStyle(style);
				// sheet.autoSizeColumn(cell.getColumnIndex());
			}
			tamanhoAnterior+= header.length;

		}
	}

	public static void addConteudoNewMerc (List<MediasNovaEmpresaVO> dados, 
			Sheet sheet, Workbook workbook, Row row10,Map<String, Integer> descRenumMap 
			) {
		dados.removeIf(c -> c.getDescRenum().equals("SBM - Salário Base Mensal"));

		dados.sort(Comparator.comparing(c -> descRenumMap.get(c.getDescRenum())));

		for (Map.Entry<String, Integer> entry : descRenumMap.entrySet()) {
			String description = entry.getKey();
			boolean found = false;

			// Verificar se a descrição já existe em mediasVOList
			for (MediasNovaEmpresaVO mediasVO : dados) {
				if (mediasVO.getDescRenum().equals(description)) {
					found = true;
					break;
				}
			}

			// Se não encontrado, adicionar um novo objeto a mediasVOList
			if (!found) {
				MediasNovaEmpresaVO newMediasVO = new MediasNovaEmpresaVO();
				newMediasVO.setDescRenum(description);
				//newMediasVO.setDescricaoDoFiltro("");
				newMediasVO.setMedia(0);
				newMediasVO.setNomeCargo("");
				newMediasVO.setNumParticipantes(0);
				newMediasVO.setP10(0);
				newMediasVO.setP25(0);
				newMediasVO.setP50(0);
				newMediasVO.setP75(0);
				newMediasVO.setP90(0);
				newMediasVO.setQtdEmpresas(0);
				newMediasVO.setSuaEmpresa(0);
				//newMediasVO.setVersion(0);
				newMediasVO.setNomeCargoXr("");
				newMediasVO.setMatricula("");
				newMediasVO.setGradeEmpresa("");
				newMediasVO.setCodigoCargo("");
				dados.add(newMediasVO);
			}
		}

		int tamanhoAnterior = 0;
		String[] header= {};
		int posicaoSBM = 0;

		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
		XSSFColor color = new XSSFColor(new byte[]{(byte) 162, 50, 50}, new DefaultIndexedColorMap());
		XSSFCellStyle  style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER,
				HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);

		for(int k=0; k<dados.size();k++) {

			String descRenum = dados.get(k).getDescRenum();

			switch (descRenum) {
			case "SBA - Salário Base Anual":
				header = new String[] {
						dados.get(k).getMercado(), // Mercado
						dados.get(k).getNmFamilia(), // Família
						dados.get(k).getNmSubFamilia(), // Subfamilia
						dados.get(k).getNomeCargoXr(), // Cargo APTAXR
						intToString(dados.get(k).getGrade()),
						intToString(dados.get(k).getQtdEmpresas()), // Quantidade de Empresas
						formatarMonetario(dados.get(k).getP25()), // Percentil 25 (P25)
						formatarMonetario(dados.get(k).getP50()), // Percentil 50 (P50)
						formatarMonetario(dados.get(k).getP75()), // Percentil 75 (P75)
						formatarMonetario(dados.get(k).getMedia()) // Média
				};
				posicaoSBM = k;
				break;

			case "ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)":
				double v = dados.get(k).getSuaEmpresa();
				double v2 = dados.get(posicaoSBM).getSuaEmpresa();

				double suaEmpresa = (v2 != 0) ? v : 0;

				double p25 = dados.get(k).getP25();
				double p25v2 = dados.get(posicaoSBM).getP25();

				double p25Final = (p25v2 != 0) ? p25  : 0;

				double p50 = dados.get(k).getP50();
				double p50v2 = dados.get(posicaoSBM).getP50();

				double p50Final = (p50v2 != 0) ? p50 : 0;

				double p75 = dados.get(k).getP75();
				double p75v2 = dados.get(posicaoSBM).getP75();

				double p75Final = (p75v2 != 0) ? p75 : 0;

				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(p25Final),
						formatarMonetario(p50Final),
						formatarMonetario(p75Final),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;    

			case "ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)":
				double vICP = dados.get(k).getSuaEmpresa();
				double v2ICP = dados.get(posicaoSBM).getSuaEmpresa();

				double suaEmpresaICP = (v2ICP != 0) ? vICP  : 0;

				double p25ICP = dados.get(k).getP25();
				double p25v2ICP = dados.get(posicaoSBM).getP25();

				double p25FinalICP = (p25v2ICP != 0) ? p25ICP  : 0;

				double p50ICP = dados.get(k).getP50();
				double p50v2ICP = dados.get(posicaoSBM).getP50();

				double p50FinalICP = (p50v2ICP != 0) ? p50ICP : 0;

				double p75ICP = dados.get(k).getP75();
				double p75v2ICP = dados.get(posicaoSBM).getP75();

				double p75FinalICP = (p75v2ICP != 0) ? p75ICP  : 0;

				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(p25FinalICP),
						formatarMonetario(p50FinalICP),
						formatarMonetario(p75FinalICP),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "TD - Total em Dinheiro":
				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "TDA - Total em Dinheiro Alvo":
				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "RDA - Remuneração Direta Alvo":
				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "RD - Remuneração Direta":
				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;

			case "ILP - Incentivos de Longo Prazo":
				header = new String[] { 
						intToString(dados.get(k).getQtdEmpresas()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()),
						formatarMonetario(dados.get(k).getMedia())
				};
				break;
			}


			// Criação de fontes e cores
			Font fontA10Black = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
			Font fontA10BlackBold = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
			XSSFColor colorGray = rgbToXSSFColor(242, 242, 242);
			XSSFColor colorWhite = rgbToXSSFColor(255, 255, 255);

			// Criação dos estilos
			XSSFCellStyle styleGrayLeft = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.LEFT, fontA10Black, colorGray, FillPatternType.SOLID_FOREGROUND, 0);
			XSSFCellStyle styleGrayCenter = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10BlackBold, colorGray, FillPatternType.SOLID_FOREGROUND, 0);
			XSSFCellStyle styleWhiteCenter = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10Black, colorWhite, FillPatternType.SOLID_FOREGROUND, 0);

			for (int i = 0; i < header.length; i++) {
				if (header.length == 15) {
					if (i <= 8) {
						style = styleGrayLeft;
					} else {
						style = styleWhiteCenter;
					}
				} else {
					if (i == 0) {
						style = styleGrayCenter;
					} else {
						style = styleWhiteCenter;
					}
				}
				Cell cell = createCells(sheet, i + tamanhoAnterior, header[i], row10);
				cell.setCellStyle(style);
				// sheet.autoSizeColumn(cell.getColumnIndex());
			}
			tamanhoAnterior+= header.length;

		}
	}


	public static void addConteudo (List<MediasVO> dados, 
			String cargoEscolhido,
			String SubFamilia,
			String familia, 
			Sheet sheet, Workbook workbook, Row row10
			) {



		Map<String, Integer> descRenumMap = new HashMap<>();
		descRenumMap.put("SBM - Salário Base Mensal", 1);
		descRenumMap.put("ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)", 2);
		descRenumMap.put("ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)", 3);
		descRenumMap.put("TDA - Total em Dinheiro Alvo", 4);
		descRenumMap.put("TD - Total em Dinheiro", 5);
		descRenumMap.put("RDA - Remuneração Direta Alvo", 6);
		descRenumMap.put("RD - Remuneração Direta", 7);
		descRenumMap.put("ILP - Incentivos de Longo Prazo", 8);

		dados.removeIf(c -> c.getDescRenum().equals("SBA - Salário Base Anual"));

		dados.sort(Comparator.comparing(c -> descRenumMap.get(c.getDescRenum())));


		for (Map.Entry<String, Integer> entry : descRenumMap.entrySet()) {
			String description = entry.getKey();
			boolean found = false;

			// Verificar se a descrição já existe em mediasVOList
			for (MediasVO mediasVO : dados) {
				if (mediasVO.getDescRenum().equals(description)) {
					found = true;
					break;
				}
			}

			// Se não encontrado, adicionar um novo objeto a mediasVOList
			if (!found) {
				MediasVO newMediasVO = new MediasVO();
				newMediasVO.setDescRenum(description);
				newMediasVO.setDescricaoDoFiltro("");
				newMediasVO.setMedia(0);
				newMediasVO.setNomeCargo(cargoEscolhido);
				newMediasVO.setNum_participantes(0);
				newMediasVO.setP10(0);
				newMediasVO.setP25(0);
				newMediasVO.setP50(0);
				newMediasVO.setP75(0);
				newMediasVO.setP90(0);
				newMediasVO.setQtd_empresas(1);
				newMediasVO.setSua_empresa(0);
				newMediasVO.setVersion(0);
				newMediasVO.setNomeCargoEmpresa(cargoEscolhido);
				dados.add(newMediasVO);
			}
		}

		int tamanhoAnterior = 0;
		String[] header= {};
		int posicaoSBM = 0;

		Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
		XSSFColor color = new XSSFColor(new byte[]{(byte) 162, 50, 50}, new DefaultIndexedColorMap());
		XSSFCellStyle  style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER,
				HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);

		for(int k=0; k<dados.size();k++) {

			String descRenum = dados.get(k).getDescRenum();

			switch (descRenum) {
			case "SBM - Salário Base Mensal":
				header = new String[] { familia,SubFamilia,dados.get(k).getNomeCargo(), dados.get(k).getNomeCargoEmpresa(),
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75())};
				posicaoSBM = k;
				break;

			case "ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)":
				double v = dados.get(k).getSua_empresa();
				double v2 = dados.get(posicaoSBM).getSua_empresa();

				double suaEmpresa = (v2 != 0) ? v : 0;

				double p25 = dados.get(k).getP25();
				double p25v2 = dados.get(posicaoSBM).getP25();

				double p25Final = (p25v2 != 0) ? p25  : 0;

				double p50 = dados.get(k).getP50();
				double p50v2 = dados.get(posicaoSBM).getP50();

				double p50Final = (p50v2 != 0) ? p50 : 0;

				double p75 = dados.get(k).getP75();
				double p75v2 = dados.get(posicaoSBM).getP75();

				double p75Final = (p75v2 != 0) ? p75 : 0;

				header = new String[] { 
						formatarMonetario(suaEmpresa),
						formatarMonetario(p25Final),
						formatarMonetario(p50Final),
						formatarMonetario(p75Final) };
				break;	
			case "ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)":
				double vICP = dados.get(k).getSua_empresa();
				double v2ICP = dados.get(posicaoSBM).getSua_empresa();

				double suaEmpresaICP = (v2ICP != 0) ? vICP  : 0;

				double p25ICP = dados.get(k).getP25();
				double p25v2ICP = dados.get(posicaoSBM).getP25();

				double p25FinalICP = (p25v2ICP != 0) ? p25ICP  : 0;

				double p50ICP = dados.get(k).getP50();
				double p50v2ICP = dados.get(posicaoSBM).getP50();

				double p50FinalICP = (p50v2ICP != 0) ? p50ICP : 0;

				double p75ICP = dados.get(k).getP75();
				double p75v2ICP = dados.get(posicaoSBM).getP75();

				double p75FinalICP = (p75v2ICP != 0) ? p75ICP  : 0;

				header = new String[] { 
						formatarMonetario(suaEmpresaICP),
						formatarMonetario(p25FinalICP),
						formatarMonetario(p50FinalICP),
						formatarMonetario(p75FinalICP) };
				break;



			case "TD - Total em Dinheiro":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()) };
				break;

			case "TDA - Total em Dinheiro Alvo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()) };
				break;
			case "RDA - Remuneração Direta Alvo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()) };
				break;
			case "RD - Remuneração Direta":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()) };
				break;
			case "ILP - Incentivos de Longo Prazo":
				header = new String[] { 
						formatarMonetario(dados.get(k).getSua_empresa()),
						formatarMonetario(dados.get(k).getP25()),
						formatarMonetario(dados.get(k).getP50()),
						formatarMonetario(dados.get(k).getP75()) 
				};
				break;	

			}


			for (int i = 0; i < header.length; i++) {
				if(header.length==8) {
					if(i==3) {
						fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
						color =  rgbToXSSFColor(242,242,242);
						style = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
								fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
					}else {
						fontA10 = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
						color =  rgbToXSSFColor(255,255,255);
						style = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
								fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
					}
				}else {
					if(i==0) {
						fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
						color =  rgbToXSSFColor(242,242,242);
						style = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
								fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
					}else {
						fontA10 = createFont(sheet, "Helvetica", (short) 12, false, IndexedColors.BLACK.getIndex());
						color =  rgbToXSSFColor(255,255,255);
						style = createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
								fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
					}
				}
				Cell cell = createCells(sheet, i+tamanhoAnterior, header[i], row10);
				cell.setCellStyle(style);
				////sheet.autoSizeColumn(cell.getColumnIndex());
			}
			tamanhoAnterior+= header.length;

		}
	}

	public static void exportarPlanilhav4(List<MediasVO> dados, String nomeEmpresa,
			String cargoEscolhido, String Familia, String Subfamilia, int ExportOption,
			List<String> distinctCargos, String mercadoEscolhido,
			Integer gradeMinimoPadrao, Integer gradeMaximoPadrao,
			UserEntity user, TbPesquisaService tbPesquisaService, EntityManager em) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String imagePath = servletContext.getRealPath("/resources/images/logoplanilha.JPG");

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=planilha.xlsx");

		// Utilize SXSSFWorkbook para manejar grandes volumes de dados
		try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
			workbook.setCompressTempFiles(true);
			Sheet sheet = workbook.createSheet("Planilha");

			// Remove as gridlines da planilha
			removeGridlines(sheet);

			// Adiciona a imagem
			addImage(sheet, imagePath);

			// Adiciona o cabeçalho
			if(ExportOption==4) {
				addHeaderNewMerc(sheet, nomeEmpresa, "", workbook, ExportOption, "");
			}else {
				addHeaderNew(sheet, nomeEmpresa, "", workbook, ExportOption, "");
			}

			Row row10 = createRows(sheet, 10, (short) 400);
			List<MediasNovaEmpresaVO> lista = new ArrayList<MediasNovaEmpresaVO>();
			//List<MediasNovaEmpresaVO> lista = tbPesquisaService.findExtracaoEmpresaPorNome(user.getIdEmpresa().getDescEmpresa());
			if(ExportOption==4) {
			//	lista = tbPesquisaService.findExtracaoEmpresaMercado(user.getIdEmpresa().getDescEmpresa());
			}else {
			//	lista = tbPesquisaService.findExtracaoEmpresaPorNome(user.getIdEmpresa().getDescEmpresa(), Familia, Subfamilia);
			}

			// Ordena a lista original por NomeCargo, NomeCargoXr, Matricula e Grade

			if(ExportOption==4){

			}else {
				lista.sort(Comparator.comparing(MediasNovaEmpresaVO::getNomeCargoXr)
						.thenComparing(MediasNovaEmpresaVO::getGrade));
			}


			// Map para agrupar por NomeCargo e NomeCargoXR com ordem preservada
			Map<String, List<MediasNovaEmpresaVO>> groupedByNomeCargoAndXR = new LinkedHashMap<>();

			// Agrupa os itens por NomeCargo e NomeCargoXR preservando a ordem
			for (MediasNovaEmpresaVO item : lista) {
				String compositeKey = item.getNomeCargo() + "|" + item.getNomeCargoXr() + 
						"|" + item.getMatricula() + "|" + item.getGrade();
				groupedByNomeCargoAndXR.putIfAbsent(compositeKey, new ArrayList<>());
				groupedByNomeCargoAndXR.get(compositeKey).add(item);
			}

			System.out.println("iniciou excel");

			Map<String, Integer> descRenumMap = new HashMap<>();
			descRenumMap.put("SBA - Salário Base Anual", 1);
			descRenumMap.put("ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)", 2);
			descRenumMap.put("ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)", 3);
			descRenumMap.put("TDA - Total em Dinheiro Alvo", 4);
			descRenumMap.put("TD - Total em Dinheiro", 5);
			descRenumMap.put("ILP - Incentivos de Longo Prazo", 6);
			descRenumMap.put("RDA - Remuneração Direta Alvo", 7);
			descRenumMap.put("RD - Remuneração Direta", 8);

			// Processa cada grupo separadamente
			int rowIndex = 10;
			for (Map.Entry<String, List<MediasNovaEmpresaVO>> entry : groupedByNomeCargoAndXR.entrySet()) {
				List<MediasNovaEmpresaVO> sublist = entry.getValue();

				row10 = createRows(sheet, rowIndex, (short) 400);
				if(ExportOption==4) {
					addConteudoNewMerc(sublist, sheet, workbook, row10, descRenumMap);
				}else {
					addConteudoNew(sublist, sheet, workbook, row10, descRenumMap);
				}
				rowIndex++;
			}

			for (int i = 0; i <= 57; i++) { 

				if(ExportOption==4) {
					if (i >= 5) { // Verifica se a coluna é a partir da J (índice 9)
						sheet.setColumnWidth(i, 7000);
					} else {
						if (i == 0) {
							sheet.setColumnWidth(i, 6000);
						} else if (i == 2) {
							sheet.setColumnWidth(i, 24000);
						} else if (i == 3) {
							sheet.setColumnWidth(i, 26000);
						} else if (i == 4) {
							sheet.setColumnWidth(i, 6000);
						} else if (i == 5) {
							sheet.setColumnWidth(i, 5000);
						}
						else {
							sheet.setColumnWidth(i, 11000);
						}
					}
				}else {
					if (i >= 9) { // Verifica se a coluna é a partir da J (índice 9)
						sheet.setColumnWidth(i, 7000);
					} else {
						if (i == 0) {
							sheet.setColumnWidth(i, 6000);
						} else if (i == 2) {
							sheet.setColumnWidth(i, 24000);
						} else if (i == 3) {
							sheet.setColumnWidth(i, 26000);
						} else if (i == 4) {
							sheet.setColumnWidth(i, 6000);
						} else if (i == 5) {
							sheet.setColumnWidth(i, 5000);
						} else if (i == 6) {
							sheet.setColumnWidth(i, 3000);
						} else if (i == 8) {
							sheet.setColumnWidth(i, 4000);
						} else {
							sheet.setColumnWidth(i, 11000);
						}
					}
				}
			}

			applyAutoFilterToAllColumns(sheet);
			
			
			// Escreve a planilha na resposta HTTP
			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
				out.flush();
			}

			System.out.println("processou excel new");

			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeColumn(Sheet sheet, int columnIndexToRemove) {
		int lastColumn = sheet.getRow(0).getLastCellNum() - 1;
		for (Row row : sheet) {
			for (int col = columnIndexToRemove; col < lastColumn; col++) {
				Cell cellToKeep = row.getCell(col + 1);
				Cell cellToReplace = row.getCell(col);

				if (cellToReplace == null) {
					cellToReplace = row.createCell(col);
				}

				if (cellToKeep != null) {
					copyCell(cellToKeep, cellToReplace);
				} else {
					row.removeCell(cellToReplace);
				}
			}
			Cell lastCell = row.getCell(lastColumn);
			if (lastCell != null) {
				row.removeCell(lastCell);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void copyCell(Cell source, Cell target) {
		target.setCellType(source.getCellType());

		switch (source.getCellType()) {
		case STRING:
			target.setCellValue(source.getStringCellValue());
			break;
		case NUMERIC:
			target.setCellValue(source.getNumericCellValue());
			break;
		case BOOLEAN:
			target.setCellValue(source.getBooleanCellValue());
			break;
		case FORMULA:
			target.setCellFormula(source.getCellFormula());
			break;
		case BLANK:
			target.setBlank();
			break;
		default:
			break;
		}

		// Copy cell styles if needed
		if (source.getCellStyle() != null) {
			target.setCellStyle(source.getCellStyle());
		}
	}

	public static void exportarPlanilhav2(List<MediasVO> dados, String nomeEmpresa,
			String cargoEscolhido, String Familia, String Subfamilia, int ExportOption,
			List<String> distinctCargos,
			String mercadoEscolhido,
			Integer gradeMinimoPadrao,
			Integer gradeMaximoPadrao,
			UserEntity user,
			TbPesquisaService tbPesquisaService, EntityManager em) {
		cargoEscolhido = formataCargo(cargoEscolhido);
		nomeEmpresa = formataCargo(nomeEmpresa);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String imagePath = servletContext.getRealPath("/resources/images/logoplanilha.JPG");

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=planilha.xlsx");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Planilha");

			// Remove as gridlines da planilha
			removeGridlines(sheet);

			// Adiciona a imagem
			addImage(sheet, imagePath);



			// Adiciona o cabeçalho
			addHeader(sheet, cargoEscolhido, nomeEmpresa,workbook, ExportOption,Subfamilia );
			Row row10 = createRows(sheet, 10,(short) 400);


			if(ExportOption==2) {
				List<MediasVO> lista = new ArrayList<MediasVO>();
				for(int i=0;i<lista.size();i++) {
					Exportar ex = new 
							Exportar();
					lista = ex.getListaEmMassa(true, distinctCargos.get(i), Familia, 
							Subfamilia, mercadoEscolhido,
							gradeMinimoPadrao, gradeMaximoPadrao, user,tbPesquisaService,em);
					row10 = createRows(sheet, i+10,(short) 400);
					addConteudo(lista, distinctCargos.get(i), Subfamilia, Familia, sheet, workbook,row10);
				}



			}else {
				addConteudo(dados, cargoEscolhido, Subfamilia, Familia, sheet, workbook,row10);

			}



			// Escreve a planilha na resposta HTTP
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();

			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void exportarPlanilhav3(List<MediasVO> dados, String nomeEmpresa,
			String cargoEscolhido, String Familia, String Subfamilia, int ExportOption,
			List<String> distinctCargos,
			UserEntity user,
			TbPesquisaService tbPesquisaService,List<EmpresasDetalheVO> listDetalhes,
			String mercadoEscolhido,
			Integer gradeMinimoPadrao,
			Integer gradeMaximoPadrao, EntityManager em) {
		//cargoEscolhido = formataCargo(cargoEscolhido);
		nomeEmpresa = formataCargo(nomeEmpresa);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String imagePath = servletContext.getRealPath("/resources/images/logoplanilha.JPG");

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=planilha.xlsx");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Planilha");

			// Remove as gridlines da planilha
			removeGridlines(sheet);

			// Adiciona a imagem
			addImage(sheet, imagePath);

			Exportar ex = new 
					Exportar();

			// Adiciona o cabeçalho
			addHeader(sheet, nomeEmpresa, nomeEmpresa,workbook, ExportOption,Subfamilia );
			Row row10 = createRows(sheet, 10,(short) 400);


			Map<String, Integer> descRenumMap = UtilExcel.createDescRenumMap();
			for(int i=0;i<listDetalhes.size();i++) {

				long startTime = System.nanoTime();
				dados.addAll(ex.getListaEmMassa(true,  
						listDetalhes.get(i).getNomeCargoEmpresa(),
						listDetalhes.get(i).getNmFamilia(), 
						listDetalhes.get(i).getNmSubFam(), 
						mercadoEscolhido,gradeMinimoPadrao,
						gradeMaximoPadrao, user,tbPesquisaService,em));
				long stopTime = System.nanoTime();



				System.out.println((stopTime - startTime) / 1000000);



				Map<String, List<MediasVO>> particoes = dados.stream()
						.collect(Collectors.groupingBy(MediasVO::getNomeCargoEmpresa));


				for (Map.Entry<String, List<MediasVO>> entry : particoes.entrySet()) {
					String nomeCargoEmpresa = entry.getKey();
					List<MediasVO> particao = entry.getValue();
					dados = UtilExcel.processDados(particao, nomeCargoEmpresa, descRenumMap);
				}
				row10 = createRows(sheet, i + 10, (short) 400);
				UtilExcel.addConteudo(dados, listDetalhes.get(i).getNomeCargoEmpresa(),
						listDetalhes.get(i).getNmSubFam(),
						listDetalhes.get(i).getNmFamilia(),
						sheet, workbook, row10);
				dados.clear();

			}



			// Escreve a planilha na resposta HTTP
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();

			context.responseComplete();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public  List<MediasVO> getListaEmMassa(boolean UsaSlider, String cargoEscolhido,
			String familiaEscolhida, String  subFamiliaEscolhida,
			String mercadoEscolhido, int gradeMinimoPadrao,
			int gradeMaximoPadrao, UserEntity user,TbPesquisaService tbPesquisaService, EntityManager em
			) {




		List<Integer> distinctGrade = new ArrayList<Integer>();
		List<MediasVO> listaDeMedias = new ArrayList<MediasVO>();
		int gradeMinimo = 0;
		int gradeMaximo = 0;
		int gradeMinimoEmpresa = 0;
		int gradeMaximoEmpresa = 0;


		if ((familiaEscolhida != null) &&  (subFamiliaEscolhida != null)
				&& (cargoEscolhido!=null) && (mercadoEscolhido!=null)){

			distinctGrade = tbPesquisaService.
					findDistinctTbPesquisaGrade(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido, mercadoEscolhido);

			if (distinctGrade!=null) {

				if(distinctGrade.size()==0) {
				}else {

					gradeMinimoPadrao = distinctGrade.get(0);
					gradeMaximoPadrao = (Integer) (distinctGrade.get(distinctGrade.size()-1));
				}
				if((UsaSlider==true) && (distinctGrade.size()>0)) {

					gradeMinimo= distinctGrade.get(0);
					gradeMaximo= (Integer) (distinctGrade.get(distinctGrade.size()-1));
				}else {

				}

				if(user==null) {
					listaDeMedias.addAll(tbPesquisaService.findMedia(familiaEscolhida,
							subFamiliaEscolhida,
							cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo));
				} else {
					if(user.getIdEmpresa()==null) {
						listaDeMedias.addAll(tbPesquisaService.findMedia(familiaEscolhida,
								subFamiliaEscolhida,
								cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo));
					}else {

						Integer existeEmpresaPres = 1;

						if(cargoEscolhido.equals("PRESIDENTE / CEO") || cargoEscolhido.equals("PRESIDENTE DE SUBSIDIÁRIA")) {
							existeEmpresaPres = tbPesquisaService
									.findCountSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase()
											,cargoEscolhido);
						}

						if(existeEmpresaPres==1) {



							Integer MaiorGradeSuaEmpresa = tbPesquisaService.
									findSuaEmpresaMaiorGrade(familiaEscolhida, subFamiliaEscolhida, cargoEscolhido, 
											mercadoEscolhido, gradeMinimo, gradeMaximo, user.getIdEmpresa().getDescEmpresa().toUpperCase());

							List<MediasVO> listaDeMediasRetorno = new ArrayList<MediasVO>();


							listaDeMediasRetorno = tbPesquisaService.findMediaSuaEmpresaNew(familiaEscolhida,
									subFamiliaEscolhida,
									cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo,
									user.getIdEmpresa().getDescEmpresa().toUpperCase(),MaiorGradeSuaEmpresa,em);

							if(listaDeMediasRetorno==null) {

							}else {
								listaDeMedias.addAll(listaDeMediasRetorno);
								List<GradeVO> listaGradesEmpresas = tbPesquisaService
										.findGradeSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase(), cargoEscolhido);
								if(listaGradesEmpresas.size()>0) {
									gradeMinimoEmpresa = listaGradesEmpresas.get(0).getGradeMinimoEmpresa();
									gradeMaximoEmpresa= listaGradesEmpresas.get(0).getGradeMaximoEmpresa();

								}
							}






						}else {
						}
					}
				}



			}else {
			}
		}
		return listaDeMedias;
	}

	public static void exportarPlanilha(List<MediasVO> dados, String nomeEmpresa, String cargoEscolhido) {
		cargoEscolhido = formataCargo(cargoEscolhido);
		nomeEmpresa = formataCargo(nomeEmpresa);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String imagePath = servletContext.getRealPath("/resources/images/logoplanilha.JPG");

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=planilha.xlsx");

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Planilha");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

			// Remove as gridlines da planilha
			sheet.setDisplayGridlines(false);

			// Adiciona a imagem
			Row row0 = sheet.createRow(0);
			Cell cell0 = row0.createCell(0);
			BufferedImage image = ImageIO.read(new File(imagePath));
			int imageWidth = image.getWidth();
			int imageHeight = (int) (image.getHeight());
			sheet.getRow(0).setHeight((short) (imageHeight * 6));

			byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
			int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = drawing.createAnchor(0, 0, imageWidth, imageHeight, 0, 0, 3, 1);
			Picture picture = drawing.createPicture(anchor, pictureIndex);
			picture.resize(1,1.3);

			// Adiciona o conteúdo da variável "cargoEscolhido" com o sufixo "X Mercado" na célula A5
			Row row4 = sheet.createRow(4);
			// Aumenta a altura da célula A5
			row4.setHeight((short) 600);


			Cell cellA5 = row4.createCell(0);
			cellA5.setCellValue(cargoEscolhido + " X Mercado");

			// Mescla as células A5 até AC5
			CellRangeAddress mergedRegion = new CellRangeAddress(4, 4, 0, 28);
			sheet.addMergedRegion(mergedRegion);

			// Define a fonte como Helvetica tamanho 12 em negrito
			CellStyle styleA5 = workbook.createCellStyle();
			styleA5.setVerticalAlignment(VerticalAlignment.CENTER);
			styleA5.setAlignment(HorizontalAlignment.LEFT);
			Font fontA5 = workbook.createFont();
			fontA5.setFontName("Helvetica");
			fontA5.setFontHeightInPoints((short) 12);
			fontA5.setBold(true);
			styleA5.setFont(fontA5);

			cellA5.setCellStyle(styleA5);

			//sheet.autoSizeColumn(cellA5.getColumnIndex());


			Row row5 = sheet.createRow(5);
			row5.setHeight((short) 600);

			Cell cellA6 = row5.createCell(0);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = formatter.format(new Date());

			cellA6.setCellValue("" + nomeEmpresa + ", " + formattedDate);

			// Define a fonte como Calibri tamanho 11
			CellStyle styleA6 = workbook.createCellStyle();
			styleA6.setVerticalAlignment(VerticalAlignment.CENTER);
			styleA6.setAlignment(HorizontalAlignment.LEFT);
			Font fontA6 = workbook.createFont();
			fontA6.setFontName("Calibri");
			fontA6.setFontHeightInPoints((short) 11);
			styleA6.setFont(fontA6);
			cellA6.setCellStyle(styleA6);
			//sheet.autoSizeColumn(cellA6.getColumnIndex());
			Row row9 = sheet.createRow(9);
			row9.setHeight((short) 400);
			Cell cellA10 = row9.createCell(0);
			cellA10.setCellValue("Cargo Pesquisa ");

			// Define a fonte como Helvetica tamanho 12
			XSSFCellStyle  styleA10 =  (XSSFCellStyle) workbook.createCellStyle();
			styleA10.setVerticalAlignment(VerticalAlignment.CENTER);
			styleA10.setAlignment(HorizontalAlignment.LEFT);


			XSSFColor color = new XSSFColor(new byte[]{(byte) 162, 50, 50}, new DefaultIndexedColorMap());
			styleA10.setFillForegroundColor(color);
			styleA10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font fontA10 = workbook.createFont();
			fontA10.setFontName("Helvetica");
			fontA10.setFontHeightInPoints((short) 12);
			fontA10.setColor(IndexedColors.WHITE.getIndex());
			fontA10.setBold(true);
			styleA10.setFont(fontA10);

			cellA10.setCellStyle(styleA10);

			sheet.setAutoFilter(new CellRangeAddress(9, sheet.getLastRowNum(), 0, 0));
			//sheet.autoSizeColumn(cellA10.getColumnIndex());

			sheet.setColumnWidth(0, 6000); // A
			sheet.setColumnWidth(1, 4000); // B
			sheet.setColumnWidth(2, 4000); // C
			sheet.setColumnWidth(3, 4000); // D


			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
				out.flush();
			}

			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
