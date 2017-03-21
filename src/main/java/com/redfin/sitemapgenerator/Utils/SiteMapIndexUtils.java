package com.redfin.sitemapgenerator.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;
import com.redfin.sitemapgenerator.jaxbModel.Sitemapindex;
import com.redfin.sitemapgenerator.jaxbModel.TSitemap;

public class SiteMapIndexUtils {

	private String pathIndexFile = "";

	public SiteMapIndexUtils(String pathIndexFile) {
		this.pathIndexFile = pathIndexFile;
	}

	public boolean addLinkToIndex(String uriLink) {
		return addLinkToIndex(uriLink, new Date());

	}

	public boolean addLinkToIndex(String uriLink, Date date) {
		boolean ret = true;
		try {
			JAXBContext jc = JAXBContext.newInstance(Sitemapindex.class);
			Unmarshaller u = jc.createUnmarshaller();
			File f = new File(this.pathIndexFile);
			Sitemapindex sitemapindex = (Sitemapindex) u.unmarshal(f);
			TSitemap ts = new TSitemap();
			ts.setLastmod(new W3CDateFormat().format(date));
			ts.setLoc(uriLink);
			sitemapindex.getSitemap().add(ts);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			OutputStream os = new FileOutputStream(this.pathIndexFile);
			marshaller.marshal(sitemapindex, os);

		} catch (JAXBException | FileNotFoundException e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;

	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < 100; i++) {
				JAXBContext jc = JAXBContext.newInstance(Sitemapindex.class);
				Unmarshaller u = jc.createUnmarshaller();
				File f = new File("c:/mydir/test.xml");
				Sitemapindex sitemapindex = (Sitemapindex) u.unmarshal(f);

				// /System.out.println(sitemapindex);
				TSitemap ts = new TSitemap();
				ts.setLastmod(new W3CDateFormat().format(new Date()));
				ts.setLoc("www.exampleme.it/linkto" + new Date().getTime() + ".html");
				sitemapindex.getSitemap().add(ts);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				// marshaller.marshal(sitemapindex, System.out);
				OutputStream os = new FileOutputStream("c:/mydir/test2.xml");
				marshaller.marshal(sitemapindex, os);
			}
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static WebSitemapUrl createSitemapUrl(String urlString, Date d) throws MalformedURLException {
		WebSitemapUrl url = new WebSitemapUrl(new URL("http://www.okconcorso.it"));

		Options option = new Options(urlString);
		option.lastMod(d);
		option.changeFreq(ChangeFreq.NEVER);
		url = new WebSitemapUrl(option);
		return url;

	}

	public static WebSitemapUrl createSitemapUrl(String urlString) throws MalformedURLException {
		return createSitemapUrl(urlString, new Date());

	}
}
