package org.applicationn.pesquisa.web;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.applicationn.pesquisa.vo.MediasVO;

public class UtilExcel {
	public static  Map<String, Integer> createDescRenumMap() {
	    Map<String, Integer> descRenumMap = new HashMap<>();
	    descRenumMap.put("SBM - Salário Base Mensal", 1);
	    descRenumMap.put("ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)", 2);
	    descRenumMap.put("ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)", 3);
	    descRenumMap.put("TDA - Total em Dinheiro Alvo", 4);
	    descRenumMap.put("TD - Total em Dinheiro", 5);
	    descRenumMap.put("RDA - Remuneração Direta Alvo", 6);
	    descRenumMap.put("RD - Remuneração Direta", 7);
	    descRenumMap.put("ILP - Incentivos de Longo Prazo", 8);
	    return descRenumMap;
	}

	public static  List<MediasVO> processDados(List<MediasVO> dados, String cargoEscolhido, Map<String, Integer> descRenumMap) {
	    dados.removeIf(c -> c.getDescRenum().equals("SBA - Salário Base Anual"));
	    dados.sort(Comparator.comparing(c -> descRenumMap.get(c.getDescRenum())));

	    for (String description : descRenumMap.keySet()) {
	        boolean found = dados.stream().anyMatch(mediasVO -> mediasVO.getDescRenum().equals(description));

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

	    return dados;
	}

	public static void addConteudo(List<MediasVO> dados, String cargoEscolhido, String subFamilia, String familia, Sheet sheet, Workbook workbook, Row row10) {
	    int tamanhoAnterior = 0;
	    int posicaoSBM = 0;

	    for (int k = 0; k < dados.size(); k++) {
	        String[] header = processMediasVO(dados, k, cargoEscolhido, subFamilia, familia, posicaoSBM);
	        if (header.length == 7) {
	            posicaoSBM = k;
	        }

	        tamanhoAnterior = createCellsAndSetStyle(sheet, workbook, row10, header, tamanhoAnterior);
	    }
	}

	private static String[] processMediasVO(List<MediasVO> dados, int index, String cargoEscolhido, String subFamilia, String familia, int posicaoSBM) {
	    MediasVO mediasVO = dados.get(index);
	    String descRenum = mediasVO.getDescRenum();
	    String[] header = {};

	    switch (descRenum) {
	        case "SBM - Salário Base Mensal":
	            header = new String[]{familia, subFamilia, mediasVO.getNomeCargo(),mediasVO.getNomeCargoEmpresa(),
	                    formatarMonetario(mediasVO.getSua_empresa()),
	                    formatarMonetario(mediasVO.getP25()),
	                    formatarMonetario(mediasVO.getP50()),
	                    formatarMonetario(mediasVO.getP75())};
	            break;
	        case "ICPA - Incentivo de Curto Prazo Alvo (Bônus + PLR)":
	        case "ICP - Incentivo de Curto Prazo Pago (Bônus + PLR)":
	        case "TD - Total em Dinheiro":
	        case "TDA - Total em Dinheiro Alvo":
	        case "RDA - Remuneração Direta Alvo":
	        case "RD - Remuneração Direta":
	        case "ILP - Incentivos de Longo Prazo":
	            header = getHeaderBasedOnDescRenum(dados, index, posicaoSBM);
	            break;
	    }

	    return header;
	}

	private static String[] getHeaderBasedOnDescRenum(List<MediasVO> dados, int index, int posicaoSBM) {
	    MediasVO mediasVO = dados.get(index);
	    double suaEmpresa = (dados.get(posicaoSBM).getSua_empresa() != 0) ? mediasVO.getSua_empresa() : 0;
	    double p25 = (dados.get(posicaoSBM).getP25() != 0) ? mediasVO.getP25() : 0;
	    double p50 = (dados.get(posicaoSBM).getP50() != 0) ? mediasVO.getP50() : 0;
	    double p75 = (dados.get(posicaoSBM).getP75() != 0) ? mediasVO.getP75() : 0;

	    return new String[]{
	            formatarMonetario(suaEmpresa),
	            formatarMonetario(p25),
	            formatarMonetario(p50),
	            formatarMonetario(p75)
	    };
	}
	
	public static String formatarMonetario (double valor) {
		NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String valorFormatado = formato.format(valor);
		if(valorFormatado==null) {
			valorFormatado="N/D";
		}else if (valorFormatado.equals("R$ 0,00")) {
			valorFormatado =  "N/D";
		}
		return valorFormatado;
	}

	private static int createCellsAndSetStyle(Sheet sheet, Workbook workbook, Row row, String[] header, int tamanhoAnterior) {
	    for (int i = 0; i < header.length; i++) {
	        XSSFCellStyle style = createCellStyle(sheet, workbook, i, header.length);
	        Cell cell = createCells(sheet, i + tamanhoAnterior, header[i], row);
	        cell.setCellStyle(style);
	    }

	    return tamanhoAnterior + header.length;
	}
	
	private static Cell createCells(Sheet sheet, int numeroDaCelula, String valorDaCelula, Row row) {
		Cell cell = row.createCell(numeroDaCelula);
		cell.setCellValue(valorDaCelula);
		return cell;
	}

	private static XSSFCellStyle createCellStyle(Sheet sheet, Workbook workbook, int index, int headerLength) {
	    Font font = createFont(sheet, "Helvetica", (short) 12, index == 0 || index == 3, IndexedColors.BLACK.getIndex());
	    XSSFColor color = (headerLength == 7 && index == 3) || (headerLength != 7 && index == 0) ?
	            rgbToXSSFColor(242, 242, 242) : rgbToXSSFColor(255, 255, 255);
	    return createXSSFStyleBordasEscuras(sheet, workbook, VerticalAlignment.CENTER, HorizontalAlignment.CENTER,
	            font, color, FillPatternType.SOLID_FOREGROUND, 0);
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
}
