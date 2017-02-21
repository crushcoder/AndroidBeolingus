package de.develcab.beolingus;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import de.develcab.beolingus.dto.Translation;

/**
 * Created by jb on 17.02.17.
 */
public class BeolingusRestServiceTest {
    private static final String TEST_HTML = "\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
            "<html>\n" +
            "<head>\n" +
            " <meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\">\n" +
            " <title>Abfrage : W&ouml;rterbuch / Dictionary (BEOLINGUS, TU&nbsp;Chemnitz)</title>\n" +
            " <meta name=\"description\" content=\"Abfrage : German - English translations and synonyms (BEOLINGUS Online dictionary, TU&nbsp;Chemnitz)\">\n" +
            " <meta name=\"description\" lang=\"de\" content=\"Abfrage : Deutsch - Englisch &Uuml;bersetzungen und Synonyme (BEOLINGUS Online-W&ouml;rterbuch, TU&nbsp;Chemnitz)\">\n" +
            " <meta name=\"keywords\" content=\"Abfrage\">\n" +
            " <meta name=\"keywords\" lang=\"de\" content=\"Abfrage\">\n" +
            " <script src=\"/d.js?v=1\" type=\"text/javascript\"></script>\n" +
            " <meta name=\"author\" content=\"Frank Richter\">\n" +
            " <meta name=\"HandheldFriendly\" content=\"true\">\n" +
            " <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\">\n" +
            " <link rel=\"SHORTCUT ICON\" href=\"http://dict.tu-chemnitz.de/favicon.ico\">\n" +
            " <link rel=\"apple-touch-icon\" href=\"http://dict.tu-chemnitz.de/pics/beo-apple.png\">\n" +
            " <style type=\"text/css\"><!-- tr.s1 {background-color:#eee;} --></style>\n" +
            " <link rel=\"stylesheet\" type=\"text/css\" href=\"/main.css\">\n" +
            "\n" +
            "\n" +
            "</head>\n" +
            "<body onload=\"sel_query(); setupDatarows();\" onkeydown=\"if (event && event.keyCode==27) del_query();\">\n" +
            "<div class=\"mini\">\n" +
            "<div class=\"main\">\n" +
            "<form name=\"formular\" style=\"margin:0px;\" action=\"/dings.cgi\"\n" +
            " onsubmit=\"window.document.formular.query.focus();window.document.formular.query.select()\">\n" +
            " <input name=\"lang\" value=\"de\" type=\"hidden\"><input name=\"mini\" value=\"1\" type=\"hidden\">\n" +
            " <input name=\"count\" value=\"50\" type=\"hidden\">\n" +
            " <div style=\"float:left;padding:0.2em 1em\"><a href=\"http://dict.tu-chemnitz.de/\" target=\"_blank\"><img border=\"0\" width=\"57\" height=\"57\"\n" +
            " src=\"/pics/beo-mini.png\" alt=\"BEOLINGUS\"></a></div>\n" +
            " <div>\n" +
            "  <span style=\"white-space:nowrap\"><a id=\"mini\" href=\"http://dict.tu-chemnitz.de/\" target=\"_blank\">W&ouml;rterbuch</a> - <a href=\"https://www.tu-chemnitz.de/\" target=\"_content\">TU&nbsp;Chemnitz</a></span><br />\n" +
            "  <input name=\"query\" accesskey=\"q\" class=\"mini\" style=\"width:11em;margin:0\" value=\"Abfrage\" /><input\n" +
            "   style=\"font-weight:bold;margin:0\" value=\" ? \" type=\"submit\" /><select class=\"m\" name=\"service\" title=\"Wählen Sie die Suchmethode aus\"> <optgroup label=\"Deutsch - Englisch\"> <option value=\"deen\" selected=\"selected\">De&harr;En W&ouml;rterbuch</option>\n" +
            " <option value=\"de-en\">De&rarr;En W&ouml;rterbuch</option>\n" +
            " <option value=\"en-de\">En&rarr;De W&ouml;rterbuch</option>\n" +
            " <option value=\"de-en-ex\">De&harr;En Beispielsätze</option>\n" +
            " <option value=\"dict-en\">Erkl&auml;rungen En</option>\n" +
            " <option value=\"dict-de\">Synonyme De</option>\n" +
            " <option value=\"fortune-en\">Spr&uuml;che En</option>\n" +
            " <option value=\"fortune-de\">Spr&uuml;che De</option>\n" +
            " </optgroup> <optgroup label=\"Deutsch - Spanisch\"> <option value=\"dees\">De&harr;Es W&ouml;rterbuch</option>\n" +
            " <option value=\"de-es\">De&rarr;Es W&ouml;rterbuch</option>\n" +
            " <option value=\"es-de\">Es&rarr;De W&ouml;rterbuch</option>\n" +
            " <option value=\"de-es-ex\">De&harr;Es Beispielsätze</option>\n" +
            " <option value=\"fortune-es\">Spr&uuml;che Es</option>\n" +
            " </optgroup> <optgroup label=\"Deutsch - Portugiesisch\"> <option value=\"dept\">De&harr;Pt W&ouml;rterbuch</option>\n" +
            " <option value=\"de-pt\">De&rarr;Pt W&ouml;rterbuch</option>\n" +
            " <option value=\"pt-de\">Pt&rarr;De W&ouml;rterbuch</option>\n" +
            " <option value=\"de-pt-ex\">De&harr;Pt Beispielsätze</option>\n" +
            "</optgroup></select>\n" +
            " </div>\n" +
            "</form>\n" +
            "</div>\n" +
            "\n" +
            " <table id=\"result\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "  <thead>\n" +
            "   <tr>\n" +
            "    <td width=\"15\"><br /></td>\n" +
            "    <th><img src=\"/pics/de.png\" alt=\"\" />&nbsp;Deutsch</th>\n" +
            "    <th><img src=\"/pics/en.png\" alt=\"\" />&nbsp;Englisch</th>\n" +
            "   </tr>\n" +
            "  </thead>\n" +
            "<tbody id=\"h1\" class=\"n\">\n" +
            "<tr class=\"s1\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"r\"><a href=\"/deutsch-englisch/Abfrage.html;m\"><b>Abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> (<a href=\"/deutsch-englisch/Aufforderung.html;m\">Aufforderung</a> <a href=\"/deutsch-englisch/zur.html;m\">zur</a> <a href=\"/deutsch-englisch/Eingabe.html;m\">Eingabe</a> <a href=\"/deutsch-englisch/bestimmter.html;m\">bestimmter</a> <a href=\"/deutsch-englisch/Daten.html;m\">Daten</a>) <a title=\"Computerwesen; EDV; Informatik\" href=\"/de-en/lists/comp.html\">[comp.]</a>  <a href=\"/dings.cgi?speak=de/4/2/P74niez4kyQ;text=Abfrage\" onclick=\"return s(this)\" onmouseover=\"return u('Abfrage')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"Abfrage\" border=\"0\" align=\"top\" /></a></td>\n" +
            "<td class=\"r\"><a href=\"/english-german/request.html;m\">request</a>   <a href=\"/dings.cgi?speak=en/2/3/9yA8A1qQBWU;text=request%20{noun}\" onclick=\"return s(this)\" onmouseover=\"return u('request {noun}')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"request {noun}\" border=\"0\" align=\"top\" /></a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "<tbody id=\"b1\" class=\"n\">\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Kennwortabfrage.html;m\">Kennwort<b>abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/password.html;m\">password</a> <a href=\"/english-german/request.html;m\">request</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/PIN-Abfrage.html;m\">PIN-<b>Abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span></td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/PIN.html;m\">PIN</a> <a href=\"/english-german/request.html;m\">request</a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "\n" +
            "<tbody id=\"h2\" class=\"n\">\n" +
            "<tr class=\"s2\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"r\"><a href=\"/deutsch-englisch/Abfrage.html;m\"><b>Abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> (<a href=\"/deutsch-englisch/von.html;m\">von</a> <a href=\"/deutsch-englisch/Daten.html;m\">Daten</a>) <a title=\"Computerwesen; EDV; Informatik\" href=\"/de-en/lists/comp.html\">[comp.]</a> <a href=\"/dings.cgi?speak=de/4/2/P74niez4kyQ;text=Abfrage\" onclick=\"return s(this)\" onmouseover=\"return u('Abfrage')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"Abfrage\" border=\"0\" align=\"top\" /></a></td>\n" +
            "<td class=\"r\"><a href=\"/english-german/query.html;m\">query</a>; <a href=\"/english-german/interrogation.html;m\">interrogation</a> (of <a href=\"/english-german/data.html;m\">data</a>)  <a href=\"/dings.cgi?speak=en/8/6/hlc3lDYyCWU;text=query\" onclick=\"return s(this)\" onmouseover=\"return u('query')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"query\" border=\"0\" align=\"top\" /></a> <a href=\"/dings.cgi?speak=en/3/9/r2VxLAHcVn6;text=interrogation\" onclick=\"return s(this)\" onmouseover=\"return u('interrogation')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"interrogation\" border=\"0\" align=\"top\" /></a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "\n" +
            "<tbody id=\"h3\" class=\"n\">\n" +
            "<tr class=\"s1\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"r\"><a href=\"/deutsch-englisch/Abrufen.html;m\">Abrufen</a> <span title=\"Substantiv, sächlich (das)\">{n}</span>; <a href=\"/deutsch-englisch/Abruf.html;m\">Abruf</a> <span title=\"Substantiv, männlich (der)\">{m}</span>; <a href=\"/deutsch-englisch/Abfragen.html;m\"><b>Abfrage</b>n</a> <span title=\"Substantiv, sächlich (das)\">{n}</span>; <a href=\"/deutsch-englisch/Abfrage.html;m\"><b>Abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> (<a href=\"/deutsch-englisch/von.html;m\">von</a> <a href=\"/deutsch-englisch/gespeicherten.html;m\">gespeicherten</a> <a href=\"/deutsch-englisch/Daten.html;m\">Daten</a>) <a title=\"Computerwesen; EDV; Informatik\" href=\"/de-en/lists/comp.html\">[comp.]</a> <a title=\"Psychologie\" href=\"/de-en/lists/psych.html\">[psych.]</a>  <a href=\"/dings.cgi?speak=de/2/2/GZR9YSBGKG2;text=Abruf\" onclick=\"return s(this)\" onmouseover=\"return u('Abruf')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"Abruf\" border=\"0\" align=\"top\" /></a> <a href=\"/dings.cgi?speak=de/4/2/P74niez4kyQ;text=Abfrage\" onclick=\"return s(this)\" onmouseover=\"return u('Abfrage')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"Abfrage\" border=\"0\" align=\"top\" /></a></td>\n" +
            "<td class=\"r\"><a href=\"/english-german/retrieval.html;m\">retrieval</a> (of <a href=\"/english-german/stored.html;m\">stored</a> <a href=\"/english-german/data.html;m\">data</a>)   <a href=\"/dings.cgi?speak=en/6/4/A40OQieG26w;text=retrieval%20{noun}\" onclick=\"return s(this)\" onmouseover=\"return u('retrieval {noun}')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"retrieval {noun}\" border=\"0\" align=\"top\" /></a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "<tbody id=\"b3\" class=\"n\">\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Datenabruf.html;m\">Datenabruf</a> <span title=\"Substantiv, männlich (der)\">{m}</span>; <a href=\"/deutsch-englisch/Datenabfrage.html;m\">Daten<b>abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/data.html;m\">data</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Abrufen.html;m\">Abrufen</a> <a href=\"/deutsch-englisch/von.html;m\">von</a> <a href=\"/deutsch-englisch/Systemmeldungen.html;m\">Systemmeldungen</a> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/system.html;m\">system</a> <a href=\"/english-german/message.html;m\">message</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/das.html;m\">das</a> <a href=\"/deutsch-englisch/Abrufen.html;m\">Abrufen</a> <a href=\"/deutsch-englisch/von.html;m\">von</a> <a href=\"/deutsch-englisch/W%f6rtern.html;m\">Wörtern</a> <a href=\"/deutsch-englisch/aus.html;m\">aus</a> <a href=\"/deutsch-englisch/dem.html;m\">dem</a> <a href=\"/deutsch-englisch/Ged%e4chtnis.html;m\">Gedächtnis</a> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/word.html;m\">word</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a> <a href=\"/english-german/from.html;m\">from</a> <a href=\"/english-german/the.html;m\">the</a> <a href=\"/english-german/memory.html;m\">memory</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Datenspeicherung.html;m\">Datenspeicherung</a> <a href=\"/deutsch-englisch/und.html;m\">und</a> <a href=\"/deutsch-englisch/-abfrage.html;m\">-<b>abfrage</b></a> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/information.html;m\">information</a> <a href=\"/english-german/storage.html;m\">storage</a> <a href=\"/english-german/and.html;m\">and</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a> <a href=\"/english-german//ISR/.html;m\">/ISR/</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Online-Datenabfrage.html;m\">Online-Daten<b>abfrage</b></a></td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/online.html;m\">online</a> <a href=\"/english-german/information.html;m\">information</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a>; <a href=\"/english-german/online.html;m\">online</a> <a href=\"/english-german/data.html;m\">data</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "\n" +
            "<tbody id=\"h4\" class=\"n\">\n" +
            "<tr class=\"s2\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"r\"><a href=\"/deutsch-englisch/Kontosaldo.html;m\">Kontosaldo</a> <span title=\"Substantiv, sächlich (das)\">{n}</span>; <a href=\"/deutsch-englisch/Kontostand.html;m\">Kontostand</a> <span title=\"Substantiv, männlich (der)\">{m}</span>; <a href=\"/deutsch-englisch/Kontosalden.html;m\">Kontosalden</a> <span title=\"Substantiv, Plural (die)\">{pl}</span>  <a href=\"/dings.cgi?speak=de/4/5/R_mW7jOJe3I;text=Kontostand\" onclick=\"return s(this)\" onmouseover=\"return u('Kontostand')\"><img src=\"/pics/s1.png\" width=\"16\" height=\"16\" alt=\"[anhören]\" title=\"Kontostand\" border=\"0\" align=\"top\" /></a></td>\n" +
            "<td class=\"r\"><a href=\"/english-german/account.html;m\">account</a> <a href=\"/english-german/balance.html;m\">balance</a>; <a href=\"/english-german/balance.html;m\">balance</a> <a href=\"/english-german/of.html;m\">of</a> <a href=\"/english-german/account.html;m\">account</a> </td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "<tbody id=\"b4\" class=\"n\">\n" +
            "<tr class=\"s2 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/aktueller.html;m\">aktueller</a> <a href=\"/deutsch-englisch/Kontostand.html;m\">Kontostand</a> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/current.html;m\">current</a> <a href=\"/english-german/account.html;m\">account</a> <a href=\"/english-german/balance.html;m\">balance</a> </td>\n" +
            "</tr>\n" +
            "<tr class=\"s2\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Abfrage.html;m\"><b>Abfrage</b></a> <a href=\"/deutsch-englisch/des.html;m\">des</a> <a href=\"/deutsch-englisch/kontostands.html;m\">kontostands</a></td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/balance.html;m\">balance</a> <a href=\"/english-german/inquiry.html;m\">inquiry</a></td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "\n" +
            "</table>\n" +
            "<script type=\"text/javascript\"> /* <![CDATA[ */\n" +
            "rt('result',0,'Zeige Beispiele','Verbirg Beispiele','Zeige alle Beispiele','Verbirg alle Beispiele','/pics');\n" +
            "/* ]]> */\n" +
            "</script>\n" +
            "</div><p align=\"center\">    <!-- englisch TU/MIO2 -->\n" +
            "<script type=\"text/javascript\"><!--\n" +
            "google_ad_client = \"pub-7368649899189322\";\n" +
            "google_ad_width = 200;\n" +
            "google_ad_height = 90;\n" +
            "google_ad_format = \"200x90_0ads_al_new\";\n" +
            "google_kw_type = 'broad';\n" +
            "google_kw = 'englisch übersetzungen Fremdsprache';\n" +
            "//2007-10-31: Linkblock Mini\n" +
            "google_ad_channel = \"8192077576\";\n" +
            "google_color_border=\"ffffff\"; \n" +
            "google_color_bg=\"ffffff\"; \n" +
            "google_color_link=\"0000FF\"; \n" +
            "googleAdColorDiv=\"ffffff\"; \n" +
            "googleAdColorBg=\"ffffff\";\n" +
            "google_color_text = \"464646\";\n" +
            "google_color_url = \"141414\";\n" +
            "//-->\n" +
            "</script>\n" +
            "<script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"></script>\n" +
            "<br /><br /></p></body></html>\n" +
            "\n";

    private BeolingusRestService beo;

    @Before
    public void init() throws Exception {
        TranslationStorage translationStorage = EasyMock.createMock(TranslationStorage.class);
        beo = new BeolingusRestService(translationStorage);
    }

    @Test
    public void testSuccess() {
        String searchTerm = "Abfrage";
        List<Translation> translations = beo.loadTranslation(searchTerm);

        Assert.assertNotNull(translations);
        Assert.assertFalse(translations.isEmpty());
        Assert.assertEquals("should be exactly as expected", 13, translations.size());

    }

    @Test
    public void testEmptyResult() {

    }

    @Test
    public void testMorePages() {

    }
}
