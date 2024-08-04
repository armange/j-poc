package br.com.armange;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OcrExample {

    static {
        // Load the OpenCV native library
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // Carregar a imagem usando OpenCV
        String imagePath = "/home/diego/tmp/ocr/img/cnh.jpg";
        Mat image = Imgcodecs.imread(imagePath);

        // Pré-processamento da imagem
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // Salvar a imagem pré-processada temporariamente
        String preprocessedImagePath = "/home/diego/tmp/ocr/img/cnh_preprocessada.jpg";
        Imgcodecs.imwrite(preprocessedImagePath, gray);

        // Configurar e usar o Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/home/diego/tmp/ocr/tesseract");  // Defina o caminho para o diretório tessdata
        tesseract.setLanguage("por");  // Defina o idioma para português

        try {
            String text = tesseract.doOCR(new java.io.File(preprocessedImagePath));
            System.out.println("Texto extraído: \n" + text);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
