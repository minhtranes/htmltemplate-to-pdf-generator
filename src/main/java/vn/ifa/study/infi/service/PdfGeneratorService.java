package vn.ifa.study.infi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import vn.ifa.study.infi.properties.PdfGeneratorProperties;

@Service
public class PdfGeneratorService {
    @Autowired
    private PdfGeneratorProperties props;

    public File generate(final String htmlContent, final String tempSubDir, final String filename)
            throws FileNotFoundException {

        File pdfDest = Path.of(props.getTempDir(), tempSubDir, filename)
                .toFile();

        if (!pdfDest.getParentFile()
                .exists()) {
            pdfDest.getParentFile()
                    .mkdirs();
        }

        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(pdfDest), converterProperties);
        return pdfDest;
    }

}
