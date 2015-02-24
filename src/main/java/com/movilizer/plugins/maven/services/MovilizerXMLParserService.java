package com.movilizer.plugins.maven.services;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.plugins.maven.DefaultValues;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XmlStreamWriter;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MovilizerXMLParserService {
    private JAXBContext movilizerRequestContext;
    private JAXBContext movilizerResponseContext;
    private Unmarshaller movilizerRequestUnmarshaller;
    private Marshaller movilizerRequestMarshaller;
    private Marshaller movilizerResponseMarshaller;
    private final Log mavenOutputLogger;
    private URL xmlOutputDir = getClass().getResource("resources/");

    public MovilizerXMLParserService(final Log mavenOutputLogger, String xmlOutputDir) throws JAXBException {
        assert mavenOutputLogger != null;
        this.mavenOutputLogger = mavenOutputLogger;
        if (xmlOutputDir != null) this.xmlOutputDir = getClass().getResource(xmlOutputDir);

        movilizerRequestContext = JAXBContext.newInstance(MovilizerRequest.class);
        movilizerRequestMarshaller = movilizerRequestContext.createMarshaller();
        movilizerRequestMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        movilizerRequestUnmarshaller = movilizerRequestContext.createUnmarshaller();
//        ValidationEventHandler validationEventHandler = new javax.xml.bind.helpers.DefaultValidationEventHandler();
        movilizerRequestUnmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent event) {
                mavenOutputLogger.error("Error unmarshalling Movilizer request: " + event.getMessage());
                return true;
            }
        });

        movilizerResponseContext = JAXBContext.newInstance(MovilizerResponse.class);
        movilizerResponseMarshaller = movilizerResponseContext.createMarshaller();
        movilizerResponseMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }

    public MovilizerRequest getRequestFromFile(String folder, String filename) throws IOException, URISyntaxException, JAXBException {
        Path folderPath = Paths.get(folder);
        if (!Files.isDirectory(folderPath)) {
            mavenOutputLogger.error(String.format("Folder specified is not a directory: %s.", folderPath.toAbsolutePath().toString()));
        }
        Path fullPath = Paths.get(folder, filename);
        if (!Files.exists(fullPath)) {
            mavenOutputLogger.error(String.format("request file not found for path: %s.", fullPath.toAbsolutePath().toString()));
        }
        return getRequestFromFile(fullPath.toFile());
    }

    public MovilizerRequest getRequestFromFile(File file) throws JAXBException {
        JAXBElement<MovilizerRequest> root;
        root = movilizerRequestUnmarshaller.unmarshal(new StreamSource(file), MovilizerRequest.class);
        return root.getValue();
    }

    public String printRequest(MovilizerRequest request) throws JAXBException {
        StringWriter writer = new StringWriter();
        movilizerRequestMarshaller.marshal(request, writer);
        return writer.toString();
    }

    public String printResponse(MovilizerResponse response) throws JAXBException {
        StringWriter writer = new StringWriter();
        movilizerResponseMarshaller.marshal(response, writer);
        return writer.toString();
    }

    public void saveRequestToFile(MovilizerRequest request, String fileName) throws IOException, URISyntaxException, JAXBException {
        File newFile = new File(xmlOutputDir + fileName + DefaultValues.MOVILIZER_XML_EXTENSION);
        newFile.getParentFile().mkdirs();
        saveRequestToFile(request, newFile);
    }

    public void saveRequestToFile(MovilizerRequest request, File file) throws IOException, JAXBException {
        file.getParentFile().mkdirs();
        XmlStreamWriter xmlWriter = WriterFactory.newXmlWriter(file);
        movilizerRequestMarshaller.marshal(request, xmlWriter);
    }
}
