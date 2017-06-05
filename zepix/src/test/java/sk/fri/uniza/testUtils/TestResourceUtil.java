package sk.fri.uniza.testUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public final class TestResourceUtil {

    private TestResourceUtil() {}

    public static final String POLICY_OID = "OID";
    public static final byte [] POLICY_DIGEST = "digest".getBytes();
    public static final String VALID_XML_FILEPATH = relativeResource2AbsolutePath("eph-valid.xml");
    public static final String VALID_XSD_FILEPATH = relativeResource2AbsolutePath("eph-valid.xsd");
    public static final String VALID_KEYSTORE_FILEPATH = relativeResource2AbsolutePath("keyStore/user_a_rsa.p12");


    public static String relativeResource2AbsolutePath(String fileName) {
        ClassLoader classLoader = TestResourceUtil.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile()).getPath();
    }

    public static InputStream validXMLInputStream(){
        String s = inMemoryValidXMLPostingSheet();
        return new ByteArrayInputStream(s.getBytes());
    }

    public static String inMemoryValidXMLPostingSheet() {
        return "<EPH verzia=\"3.0\" xmlns=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\">" +
                "  <InfoEPH>" +
                "    <Mena>EUR</Mena>" +
                "    <TypEPH>1</TypEPH>" +
                "    <EPHID>PRV001</EPHID>" +
                "    <Datum>20120117</Datum>" +
                "    <PocetZasielok>1</PocetZasielok>" +
                "    <DruhZasielky>4</DruhZasielky>" +
                "    <Odosielatel>" +
                "      <OdosielatelID>ABCDEFGHIJ</OdosielatelID>" +
                "      <Meno>Bc. Peter Jonaštík</Meno>" +
                "      <Organizacia>Signaturix, s.r.o.</Organizacia>" +
                "      <Ulica>Záhradná 1</Ulica>" +
                "      <Mesto>Zvolen</Mesto>" +
                "      <PSC>96001</PSC>" +
                "    </Odosielatel>" +
                "  </InfoEPH> " +
                "  <Zasielky>" +
                "    <Zasielka>" +
                "      <Adresat>" +
                "        <Meno>Xénia Tuctová</Meno>" +
                "        <Organizacia>XYZ, a. s.</Organizacia>" +
                "        <Ulica>Tržná 1</Ulica>" +
                "        <Mesto>Banská Bystrica </Mesto>" +
                "        <PSC>97401</PSC>" +
                "      </Adresat>" +
                "      <Info />" +
                "    </Zasielka>" +
                "  </Zasielky>" +
                "</EPH>";
    }

    public static InputStream invalidXMLInputStream(){
        String s = invalidXML();
        return new ByteArrayInputStream(s.getBytes());
    }

    public static String invalidXML() {
        return "<EPH verzia=\"3.0\" xmlns=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\">" +
                "  <InfoEPH>" +
                "    <!-- element 'Mena' is missing -->" +
                "    <TypEPH>1</TypEPH>" +
                "    <EPHID>PRV001</EPHID>" +
                "    <Datum>20120117</Datum>" +
                "    <PocetZasielok>1</PocetZasielok>" +
                "    <DruhZasielky>4</DruhZasielky>" +
                "    <Odosielatel>" +
                "      <OdosielatelID>ABCDEFGHIJ</OdosielatelID>" +
                "      <Meno>Bc. Peter Jonaštík</Meno>" +
                "      <Organizacia>Signaturix, s.r.o.</Organizacia>" +
                "      <Ulica>Záhradná 1</Ulica>" +
                "      <Mesto>Zvolen</Mesto>" +
                "      <PSC>96001</PSC>" +
                "    </Odosielatel>" +
                "  </InfoEPH>" +
                "  <Zasielky>" +
                "    <Zasielka>" +
                "      <Adresat>" +
                "        <Meno>Xénia Tuctová</Meno>" +
                "        <Organizacia>XYZ, a. s.</Organizacia>" +
                "        <Ulica>Tržná 1</Ulica>" +
                "        <Mesto>Banská Bystrica </Mesto>" +
                "        <PSC>97401</PSC>" +
                "      </Adresat>" +
                "      <Info />" +
                "    </Zasielka>" +
                "  </Zasielky>" +
                "</EPH>";
    }

    public static InputStream notWellFormattedXMLInputStream(){
        String s ="<EPH verzia=\"3.0\" xmlns=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\">" +
                "  <InfoEPH <!-- '>' removed -->" +
                "    <Mena>EUR</Mena>" +
                "    <TypEPH>1</TypEPH>" +
                "    <EPHID>PRV001</EPHID>" +
                "    <Datum>20120117</Datum>" +
                "    <PocetZasielok>1</PocetZasielok>" +
                "    <Odosielatel>" +
                "      <OdosielatelID>ABCDEFGHIJ</OdosielatelID>" +
                "      <Meno>Bc. Peter Jonaštík</Meno>" +
                "      <Organizacia>Signaturix, s.r.o.</Organizacia>" +
                "      <Ulica>Záhradná 1</Ulica>" +
                "      <Mesto>Zvolen</Mesto>" +
                "      <PSC>96001</PSC>" +
                "    </Odosielatel>" +
                "  </InfoEPH> " +
                "  <Zasielky>" +
                "    <Zasielka>" +
                "      <Adresat>" +
                "        <Meno>Xénia Tuctová</Meno>" +
                "        <Organizacia>XYZ, a. s.</Organizacia>" +
                "        <Ulica>Tržná 1</Ulica>" +
                "        <Mesto>Banská Bystrica </Mesto>" +
                "        <PSC>97401</PSC>" +
                "      </Adresat>" +
                "      <Info />" +
                "    </Zasielka>" +
                "  </Zasielky>" +
                "</EPH>";
        return new ByteArrayInputStream(s.getBytes());
    }
            
    public static InputStream validXSDInputStream(){
        String s= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\" elementFormDefault=\"qualified\">" +
                "  <xs:element xmlns:eph=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\" name=\"EPH\" type=\"eph:ephType\" />" +
                "  <xs:complexType name=\"ephType\">" +
                "    <xs:sequence>" +
                "      <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"InfoEPH\">" +
                "        <xs:complexType>" +
                "          <xs:sequence>" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Mena\" type=\"xs:string\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"TypEPH\" type=\"xs:positiveInteger\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"EPHID\" type=\"xs:string\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Datum\" type=\"xs:string\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"PocetZasielok\" type=\"xs:string\" />" +
                "            <xs:element minOccurs=\"0\" maxOccurs=\"unbounded\" name=\"Uhrada\">" +
                "              <xs:complexType>" +
                "                <xs:sequence>" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"SposobUhrady\" type=\"xs:positiveInteger\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"SumaUhrady\" type=\"xs:string\" />" +
                "                </xs:sequence>" +
                "              </xs:complexType>" +
                "            </xs:element>" +
                "            <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"DruhPPP\" type=\"xs:positiveInteger\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"DruhZasielky\" type=\"xs:positiveInteger\" />" +
                "            <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"SposobSpracovania\" type=\"xs:positiveInteger\" />" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Odosielatel\">" +
                "              <xs:complexType>" +
                "                <xs:sequence>" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"OdosielatelID\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Meno\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Organizacia\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Ulica\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Mesto\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"PSC\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Krajina\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Telefon\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Email\" type=\"xs:string\" />" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CisloUctu\" type=\"xs:string\" />" +
                "                </xs:sequence>" +
                "              </xs:complexType>" +
                "            </xs:element>" +
                "          </xs:sequence>" +
                "        </xs:complexType>" +
                "      </xs:element>" +
                "      <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Zasielky\">" +
                "        <xs:complexType>" +
                "          <xs:sequence>" +
                "            <xs:element minOccurs=\"1\" maxOccurs=\"unbounded\" name=\"Zasielka\">" +
                "              <xs:complexType>" +
                "                <xs:sequence>" +
                "                  <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Adresat\">" +
                "                    <xs:complexType>" +
                "                      <xs:sequence>" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Meno\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Organizacia\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Ulica\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Mesto\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"PSC\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Krajina\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Telefon\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Email\" type=\"xs:string\" />" +
                "                      </xs:sequence>" +
                "                    </xs:complexType>" +
                "                  </xs:element>" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Spat\">" +
                "                    <xs:complexType>" +
                "                      <xs:sequence>" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Meno\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Organizacia\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Ulica\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Mesto\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"PSC\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Krajina\" type=\"xs:string\" />" +
                "                      </xs:sequence>" +
                "                    </xs:complexType>" +
                "                  </xs:element>" +
                "                  <xs:element name=\"Info\">" +
                "                    <xs:complexType>" +
                "                      <xs:sequence>" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CiarovyKod\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"ZasielkaID\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Hmotnost\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CenaDobierky\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CenaPoistneho\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CenaVyplatneho\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Trieda\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"CisloUctu\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"SymbolPrevodu\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Poznamka\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"DruhPPP\" type=\"xs:positiveInteger\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"DruhZasielky\" type=\"xs:positiveInteger\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"DatumPrijatia\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"Objem\" type=\"xs:string\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"PocetKusov\" type=\"xs:positiveInteger\" />" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"ObsahZasielky\" type=\"xs:string\" />" +
                "                      </xs:sequence>" +
                "                    </xs:complexType>" +
                "                  </xs:element>" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"PouziteSluzby\">" +
                "                    <xs:complexType>" +
                "                      <xs:sequence>" +
                "                        <xs:element minOccurs=\"0\" maxOccurs=\"unbounded\" name=\"Sluzba\" type=\"xs:string\" />" +
                "                      </xs:sequence>" +
                "                    </xs:complexType>" +
                "                  </xs:element>" +
                "                  <xs:element minOccurs=\"0\" maxOccurs=\"1\" name=\"DalsieUdaje\">" +
                "                    <xs:complexType>" +
                "                      <xs:sequence>" +
                "                        <xs:element minOccurs=\"1\" maxOccurs=\"unbounded\" name=\"Udaj\">" +
                "                          <xs:complexType>" +
                "                            <xs:sequence>" +
                "                              <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Nazov\" type=\"xs:string\" />" +
                "                              <xs:element minOccurs=\"1\" maxOccurs=\"1\" name=\"Hodnota\" type=\"xs:string\" />" +
                "                            </xs:sequence>" +
                "                          </xs:complexType>" +
                "                        </xs:element>" +
                "                      </xs:sequence>" +
                "                    </xs:complexType>" +
                "                  </xs:element>" +
                "                </xs:sequence>" +
                "              </xs:complexType>" +
                "            </xs:element>" +
                "          </xs:sequence>" +
                "        </xs:complexType>" +
                "      </xs:element>" +
                "    </xs:sequence>" +
                "    <xs:attribute name=\"verzia\" type=\"xs:string\" />" +
                "  </xs:complexType>" +
                "</xs:schema>";
        return new ByteArrayInputStream(s.getBytes()); 
    }
    
    public static InputStream invalidXSDInputStream(){
        String s= invalidXSD();
        return new ByteArrayInputStream(s.getBytes()); 
    }

    public static String invalidXSD() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://ekp.posta.sk/LOGIS/Formulare/Podaj_v03\" elementFormDefault=\"qualified\">" +
                "  <xs:element" +
                "</xs:schema>";
    }

    public static InputStream dummyInputStream(){
        return new ByteArrayInputStream("test dummy".getBytes());
    }


    public static String helloWorldExampleContent() {
        return "<?xml version=\"1.0\"?><test>Hello World !</test>";
    }
}
