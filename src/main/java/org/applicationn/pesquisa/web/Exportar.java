package org.applicationn.pesquisa.web;

import java.awt.image.BufferedImage;
import java.io.File;
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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
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
import org.applicationn.pesquisa.vo.GradeVO;
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
		picture.resize(0.7, 1);
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
		sheet.autoSizeColumn(celula.getColumnIndex());
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
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setTopBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		style.setBorderRight(BorderStyle.MEDIUM);
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
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.MEDIUM);
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

		Cell cellD9 = createCells(sheet, 3, "SBM - Salário Base Mensal", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 3, 6));
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

		Cell cellD20 = createCellsQuebra(sheet, 7, content, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 7, 10));
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

		Cell cellD21 = createCellsQuebra(sheet, 11, contentICP, row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 11, 14));
		cellD21.setCellStyle(style);


		color =  rgbToXSSFColor(255,82,80);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, fontA10, 
				color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD22 = createCells(sheet, 15, "TDA - Total em Dinheiro Alvo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 15, 18));
		cellD22.setCellStyle(style);




		color =  rgbToXSSFColor(233,77,101);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD23 = createCells(sheet, 19, "TD - Total em Dinheiro", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 19, 22));
		cellD23.setCellStyle(style);



		color =  rgbToXSSFColor(128,128,128);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD24 = createCells(sheet, 23, "RDA - Remuneração Direta Alvo", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 23, 26));
		cellD24.setCellStyle(style);




		color =  rgbToXSSFColor(89,89,89);
		style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
				fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		Cell cellD25 = createCells(sheet, 27, "RD - Remuneração Direta", row8);
		addMergeRegion(sheet, new CellRangeAddress(8, 8, 27, 30));
		cellD25.setCellStyle(style);





		// criar a nona linha (PRIMEIRO HEADER)
		Row row9 = createRows(sheet, 9, (short) 400);
		String[] headerTitles = { "Família", "Subfamilia", "Cargo", "Sua empresa", "Percentil 25 (P25)",
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
					|| headerTitles[i]=="Subfamilia" || headerTitles[i]=="Cargo" 	 	) {
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

			cell.setCellStyle(style);
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
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
			sheet.autoSizeColumn(cell.getColumnIndex());
		}
	}

	public static String formatarMonetario (double valor) {
		NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String valorFormatado = formato.format(valor);
		return valorFormatado;
	}

	public static String doubleToString(double number) {
	    DecimalFormat decimalFormat = new DecimalFormat("#.#");
	    return decimalFormat.format(number);
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


		// Remova os objetos com valores nulos no atributo DescRenum
		dados.removeIf(c -> c.getDescRenum().equals("ILP - Incentivos de Longo Prazo"));
		dados.removeIf(c -> c.getDescRenum().equals("SBA - Salário Base Anual"));

		// Classifique o ArrayList usando uma expressão lambda e o método Comparator.comparing()
		dados.sort(Comparator.comparing(c -> descRenumMap.get(c.getDescRenum())));
		int tamanhoAnterior = 0;
		String[] header= {};
		int posicaoSBM = 0;
		
		for(int k=0; k<dados.size();k++) {

			 String descRenum = dados.get(k).getDescRenum();
			
			 switch (descRenum) {
				case "SBM - Salário Base Mensal":
					header = new String[] { familia,SubFamilia,dados.get(k).getNomeCargo(), 
							formatarMonetario(dados.get(k).getSua_empresa()),
							formatarMonetario(dados.get(k).getP25()),
							formatarMonetario(dados.get(k).getP50()),
							formatarMonetario(dados.get(k).getP75())};
					posicaoSBM = k;
					break;
					
				case "ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)":
					double v = dados.get(k).getSua_empresa();
					double v2 = dados.get(posicaoSBM).getSua_empresa();

					double suaEmpresa = (v2 != 0) ? v / v2 : 0;

					double p25 = dados.get(k).getP25();
					double p25v2 = dados.get(posicaoSBM).getP25();

					double p25Final = (p25v2 != 0) ? p25 / p25v2 : 0;

					double p50 = dados.get(k).getP50();
					double p50v2 = dados.get(posicaoSBM).getP50();

					double p50Final = (p50v2 != 0) ? p50 / p50v2 : 0;

					double p75 = dados.get(k).getP75();
					double p75v2 = dados.get(posicaoSBM).getP75();

					double p75Final = (p75v2 != 0) ? p75 / p75v2 : 0;
					
					header = new String[] { 
							doubleToString(suaEmpresa),
							doubleToString(p25Final),
							doubleToString(p50Final),
							doubleToString(p75Final) };
					break;	
				case "ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)":
					double vICP = dados.get(k).getSua_empresa();
					double v2ICP = dados.get(posicaoSBM).getSua_empresa();

					double suaEmpresaICP = (v2ICP != 0) ? vICP / v2ICP : 0;

					double p25ICP = dados.get(k).getP25();
					double p25v2ICP = dados.get(posicaoSBM).getP25();

					double p25FinalICP = (p25v2ICP != 0) ? p25ICP / p25v2ICP : 0;

					double p50ICP = dados.get(k).getP50();
					double p50v2ICP = dados.get(posicaoSBM).getP50();

					double p50FinalICP = (p50v2ICP != 0) ? p50ICP / p50v2ICP : 0;

					double p75ICP = dados.get(k).getP75();
					double p75v2ICP = dados.get(posicaoSBM).getP75();

					double p75FinalICP = (p75v2ICP != 0) ? p75ICP / p75v2ICP : 0;
					
					header = new String[] { 
							doubleToString(suaEmpresaICP),
							doubleToString(p25FinalICP),
							doubleToString(p50FinalICP),
							doubleToString(p75FinalICP) };
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
				
			 }
			
			Font fontA10 = createFont(sheet, "Helvetica", (short) 12, true, IndexedColors.BLACK.getIndex());
			XSSFColor color = new XSSFColor(new byte[]{(byte) 162, 50, 50}, new DefaultIndexedColorMap());
			XSSFCellStyle  style = createXSSFStyle(sheet, workbook, VerticalAlignment.CENTER,
					HorizontalAlignment.CENTER, 
					fontA10, color, FillPatternType.SOLID_FOREGROUND,0);
		
			for (int i = 0; i < header.length; i++) {
				if(header.length==7) {
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
				//System.out.println(header[i] + "indice  "+ i+ "tamanho" + tamanhoAnterior);
				cell.setCellStyle(style);
				sheet.autoSizeColumn(cell.getColumnIndex());
			}
			tamanhoAnterior+= header.length;

		}
	}


	public static void exportarPlanilhav2(List<MediasVO> dados, String nomeEmpresa,
			String cargoEscolhido, String Familia, String Subfamilia, int ExportOption,
			List<String> distinctCargos,
			String mercadoEscolhido,
			Integer gradeMinimoPadrao,
			Integer gradeMaximoPadrao,
			UserEntity user,
			TbPesquisaService tbPesquisaService) {
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
				for(int i=0;i<distinctCargos.size();i++) {
					Exportar ex = new 
							Exportar();
					lista = ex.getListaEmMassa(true, distinctCargos.get(i), Familia, 
							Subfamilia, mercadoEscolhido,
							gradeMinimoPadrao, gradeMaximoPadrao, user,tbPesquisaService);
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

	
	public  List<MediasVO> getListaEmMassa(boolean UsaSlider, String cargoEscolhido,
			String familiaEscolhida, String  subFamiliaEscolhida,
			String mercadoEscolhido, int gradeMinimoPadrao,
			int gradeMaximoPadrao, UserEntity user,TbPesquisaService tbPesquisaService
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



								listaDeMedias.addAll(tbPesquisaService.findMediaSuaEmpresa(familiaEscolhida,
										subFamiliaEscolhida,
										cargoEscolhido, mercadoEscolhido, gradeMinimo, gradeMaximo,
										user.getIdEmpresa().getDescEmpresa().toUpperCase(),MaiorGradeSuaEmpresa));

								List<GradeVO> listaGradesEmpresas = tbPesquisaService
										.findGradeSuaEmpresaPres(user.getIdEmpresa().getDescEmpresa().toUpperCase(), cargoEscolhido);
								if(listaGradesEmpresas.size()>0) {
									gradeMinimoEmpresa = listaGradesEmpresas.get(0).getGradeMinimoEmpresa();
									gradeMaximoEmpresa= listaGradesEmpresas.get(0).getGradeMaximoEmpresa();

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

			sheet.autoSizeColumn(cellA5.getColumnIndex());


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
			sheet.autoSizeColumn(cellA6.getColumnIndex());
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
			sheet.autoSizeColumn(cellA10.getColumnIndex());

			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();

			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
