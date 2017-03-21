package com.redfin.sitemapgenerator.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

public class SitemapIndexManager {
	private String path;
	public final String CONTAINER_NAME = "sitemap.xml";
	public final String CONTAINER_INNER_FOLDER = "sitemaps/";
	public final String CONTAINER_INNER_NAME = "sitemap_{name}";
	private String siteLinkName;

	public SitemapIndexManager(String path, String siteLinkName) {
		this.path = path;
		this.siteLinkName = siteLinkName;
		File direct = new File(this.path + File.separator + CONTAINER_INNER_FOLDER);
		if (!direct.exists()) {
			direct.mkdir();
		}
	}

	public boolean checkIfExistsSitemapIndex() {
		return new File(this.path + File.separator + CONTAINER_NAME).exists();
	}

	public boolean addSitemapInnerIndex(String name, String partUrl, List<String> links) {
		boolean ret = true;

		if (new File(this.path + File.separator + CONTAINER_NAME).exists()) {
			// BLOCCO SITEMAP DA AGGIORNARE
			String filenameCheck= 
					this.path  + //
					"/" + //
					CONTAINER_INNER_FOLDER + //
					CONTAINER_INNER_NAME.replace("{name}", name) +//
					".xml";
			if (! new File(filenameCheck).exists()) {
				try {
					WebSitemapGenerator wsg = WebSitemapGenerator.builder(this.siteLinkName, new File(this.path + File.separator + CONTAINER_INNER_FOLDER)).fileNamePrefix(CONTAINER_INNER_NAME.replace("{name}", name)).build();
					for (String item : links)
						wsg.addUrl(SiteMapIndexUtils.createSitemapUrl(this.siteLinkName + "/" + partUrl + item ));
					wsg.write();
				} catch (Exception ex) {

				}

				ret &= new SiteMapIndexUtils(this.path + File.separator + CONTAINER_NAME).addLinkToIndex(this.siteLinkName + "/" + CONTAINER_INNER_FOLDER + CONTAINER_INNER_NAME.replace("{name}", name) + ".xml");
			} else {
				System.out.println("file " + (filenameCheck) + " already exists");
			}
		} else {
			try {

				// BLOCCO SITEMAP NON ESISTENTE
				WebSitemapGenerator wsg = WebSitemapGenerator.builder(this.siteLinkName, new File(this.path + File.separator + CONTAINER_INNER_FOLDER)).fileNamePrefix(CONTAINER_INNER_NAME.replace("{name}", name)).build();

				for (String item : links)
					wsg.addUrl(SiteMapIndexUtils.createSitemapUrl(this.siteLinkName + "/" + partUrl + item ));
				wsg.write();

				SitemapIndexGenerator sig = new SitemapIndexGenerator(this.siteLinkName, new File(this.path + File.separator + CONTAINER_NAME));
				sig.addUrl(this.siteLinkName + "/" + CONTAINER_INNER_FOLDER + CONTAINER_INNER_NAME.replace("{name}", name) + ".xml");
				sig.write();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret = false;
			}
		}
		// http://google.com/ping?sitemap=http://www.yoururl.it/sitemap.xml
		return ret;
	}
}
