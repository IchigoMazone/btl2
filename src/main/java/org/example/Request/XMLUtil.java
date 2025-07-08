//package org.example.Request;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
//import java.io.File;
//
//public class XMLUtil {
//
//    public static <T> T readFromFile(String filePath, Class<T> clazz) {
//        try {
//            JAXBContext context = JAXBContext.newInstance(clazz);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            return (T) unmarshaller.unmarshal(new File(filePath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static <T> void writeToFile(String filePath, T data) {
//        try {
//            JAXBContext context = JAXBContext.newInstance(data.getClass());
//            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.marshal(data, new File(filePath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
