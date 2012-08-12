package WrappedElement;

public interface remotePublishDialog {

	String	NEW_LINE	= System.getProperty("line.separator");

	String	base		= "//div[@role='dialog']";
	String	close		= base + "//span[@class='aui-toolbar-content']/button[@title='Close dialog']";
	// match the dialog title
	String	title		= base + "//span[@class='aui-panel-hd-text'  and contains(.,'%TEXT%')]";

	interface options {
		String	base		= "//div[contains(@id,'_publishOptions')]";
		String	collapse	= "//a[@title='Collapse']";
		String	expand		= "//a[@title='Expand']";

		// match all the radio checkbox on the dialog
		String	radio		= "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='radio']";

		interface pages {
			String	base		= "//div[@id='layoutsAdminExportPagesPagesPanel']";

			// Pages section
			String	checkbox	= "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='checkbox']";

			interface table {
				String	base		= "//table[@class='taglib-search-iterator']";
				String	pageName	= base + "//td[contains(text(),'%TEXT%')]";
				String	change		= base + "//tr[td[contains(text(),'%TEXT%')]]//a[contains(.,'Change')]";

			}

			interface button {
				String	changeSelection	= "//div[contains(@class,'aui-button-holder')]//input[@value='Change Selection']";

			}

			interface selectPagePane {
				String	base		= "//div[contains(@id,'_pane')]";
				String	checkbox	= "//div[following-sibling::div[normalize-space(text())='%TEXT%']][input[@type='checkbox']]";

				interface button {
					String	select	= "//div[contains(@class,'aui-button-holder')]//input[@value='Select']";
				}
			}
		}

		interface applicatons {
			String	base		= "//div[@id='layoutsAdminExportPagesPortletsPanel']";

			// Applications section
			// match only the first class checkbox under Applications section
			String	checkbox	= "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[contains(@id,'PORTLET_DATA') and @type='checkbox']";

		}

		interface other {
			String	base		= "//div[@id='layoutsAdminExportPagesOptionsPanel']";

			// Other section
			// match all the checkbox in other and pages section
			String	checkbox	= "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='checkbox']";

		}

		interface connection {
			String	base	= "//div[@id='layoutsAdminExportPagesConnectionPanel']";

			// Remote connection section
			String	address	= "//input[contains(@id,'_remoteAddress') and @type='text']";
			String	port	= "//input[contains(@id,'_remotePort') and @type='text']";
			String	siteID	= "//input[contains(@id,'_remoteGroupId') and @type='text']";

		}

		interface buttons {
			String	publish	= base + "//div[@class='aui-button-holder ']//input[@value='Publish']";

		}

	}

}
