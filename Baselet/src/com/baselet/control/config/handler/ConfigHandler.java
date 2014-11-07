package com.baselet.control.config.handler;

import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

import com.baselet.control.config.Config;
import com.baselet.control.config.ConfigConst;
import com.baselet.control.constants.Constants;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.Program;
import com.baselet.control.util.Path;
import com.baselet.control.util.RecentlyUsedFilesList;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.standalone.StandaloneGUI;
import com.umlet.language.enums.FieldOptions;
import com.umlet.language.enums.MethodOptions;
import com.umlet.language.enums.SignatureOptions;
import com.umlet.language.enums.SortOptions;

public class ConfigHandler {

	private static final String PROGRAM_VERSION = "program_version";
	private static final String PROPERTIES_PANEL_FONTSIZE = "properties_panel_fontsize";
	private static final String DEFAULT_FONTSIZE = "default_fontsize";
	private static final String DEFAULT_FONTFAMILY = "default_fontfamily";
	private static final String SHOW_STICKINGPOLYGON = "show_stickingpolygon";
	private static final String SHOW_GRID = "show_grid";
	private static final String ENABLE_CUSTOM_ELEMENTS = "enable_custom_elements";
	private static final String UI_MANAGER = "ui_manager";
	private static final String PRINT_PADDING = "print_padding";
	private static final String PDF_EXPORT_FONT = "pdf_export_font";
	private static final String CHECK_FOR_UPDATES = "check_for_updates";
	private static final String OPEN_FILE_HOME = "open_file_home";
	private static final String SAVE_FILE_HOME = "save_file_home";
	private static final String DEV_MODE = "dev_mode";
	private static final String LAST_USED_PALETTE = "last_used_palette";
	private static final String MAIN_SPLIT_POSITION = "main_split_position";
	private static final String RIGHT_SPLIT_POSITION = "right_split_position";
	private static final String START_MAXIMIZED = "start_maximized";
	private static final String MAIL_SPLIT_POSITION = "mail_split_position";
	private static final String PROGRAM_SIZE = "program_size";
	private static final String PROGRAM_LOCATION = "program_location";
	private static final String RECENT_FILES = "recent_files";
	private static final String MAIL_SMTP = "mail_smtp";
	private static final String MAIL_SMTP_AUTH = "mail_smtp_auth";
	private static final String MAIL_SMTP_USER = "mail_smtp_user";
	private static final String MAIL_SMTP_PW_STORE = "mail_smtp_pw_store";
	private static final String MAIL_SMTP_PW = "mail_smtp_pw";
	private static final String MAIL_FROM = "mail_from";
	private static final String MAIL_TO = "mail_to";
	private static final String MAIL_CC = "mail_cc";
	private static final String MAIL_BCC = "mail_bcc";
	private static final String MAIL_XML = "mail_xml";
	private static final String MAIL_GIF = "mail_gif";
	private static final String MAIL_PDF = "mail_pdf";
	private static final String GENERATE_CLASS_PACKAGE = "generate_class_package";
	private static final String GENERATE_CLASS_FIELDS = "generate_class_fields";
	private static final String GENERATE_CLASS_METHODS = "generate_class_methods";
	private static final String GENERATE_CLASS_SIGNATURES = "generate_class_signatures";
	private static final String GENERATE_CLASS_SORTINGS = "generate_class_sortings";

	private static File configfile;
	private static Properties props;

	public static void loadConfig() {
		Config cfg = Config.getInstance();

		configfile = new File(Path.config());
		if (!configfile.exists()) {
			return;
		}

		props = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(Path.config());
			try {
				props.load(inputStream);
			} finally {
				inputStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		cfg.setProgramVersion(getStringProperty(PROGRAM_VERSION, Program.VERSION));
		ConfigConst.defaultFontsize = getIntProperty(DEFAULT_FONTSIZE, ConfigConst.defaultFontsize);
		Constants.propertiesPanelFontsize = getIntProperty(PROPERTIES_PANEL_FONTSIZE, Constants.propertiesPanelFontsize);
		ConfigConst.defaultFontFamily = getStringProperty(DEFAULT_FONTFAMILY, ConfigConst.defaultFontFamily);
		SharedConstants.show_stickingpolygon = getBoolProperty(SHOW_STICKINGPOLYGON, SharedConstants.show_stickingpolygon);
		ConfigConst.show_grid = getBoolProperty(SHOW_GRID, ConfigConst.show_grid);
		ConfigConst.enable_custom_elements = getBoolProperty(ENABLE_CUSTOM_ELEMENTS, ConfigConst.enable_custom_elements);
		cfg.setUiManager(getStringProperty(UI_MANAGER, cfg.getUiManager()));
		ConfigConst.printPadding = getIntProperty(PRINT_PADDING, ConfigConst.printPadding);
		ConfigConst.pdfExportFont = getStringProperty(PDF_EXPORT_FONT, ConfigConst.pdfExportFont);
		ConfigConst.checkForUpdates = getBoolProperty(CHECK_FOR_UPDATES, ConfigConst.checkForUpdates);
		cfg.setOpenFileHome(getStringProperty(OPEN_FILE_HOME, cfg.getOpenFileHome()));
		cfg.setSaveFileHome(getStringProperty(SAVE_FILE_HOME, cfg.getSaveFileHome()));
		SharedConstants.dev_mode = getBoolProperty(DEV_MODE, SharedConstants.dev_mode);
		ConfigConst.lastUsedPalette = getStringProperty(LAST_USED_PALETTE, null);
		ConfigConst.main_split_position = getIntProperty(MAIN_SPLIT_POSITION, ConfigConst.main_split_position);
		ConfigConst.right_split_position = getIntProperty(RIGHT_SPLIT_POSITION, ConfigConst.right_split_position);
		ConfigConst.mail_split_position = getIntProperty(MAIL_SPLIT_POSITION, ConfigConst.mail_split_position);
		ConfigConst.start_maximized = getBoolProperty(START_MAXIMIZED, ConfigConst.start_maximized);

		// In case of start_maximized=true we don't store any size or location information
		if (!ConfigConst.start_maximized) {
			ConfigConst.program_size = getDimensionProperty(PROGRAM_SIZE, ConfigConst.program_size);
			ConfigConst.program_location = getPointProperty(PROGRAM_LOCATION, ConfigConst.program_location);
		}

		String recentFiles = props.getProperty(RECENT_FILES);
		if (recentFiles != null) {
			RecentlyUsedFilesList.getInstance().addAll(Arrays.asList(props.getProperty(RECENT_FILES).split("\\|")));
		}

		/* Mail */
		ConfigConst.mail_smtp = getStringProperty(MAIL_SMTP, ConfigConst.mail_smtp);
		ConfigConst.mail_smtp_auth = getBoolProperty(MAIL_SMTP_AUTH, ConfigConst.mail_smtp_auth);
		ConfigConst.mail_smtp_user = getStringProperty(MAIL_SMTP_USER, ConfigConst.mail_smtp_user);
		ConfigConst.mail_smtp_pw_store = getBoolProperty(MAIL_SMTP_PW_STORE, ConfigConst.mail_smtp_pw_store);
		ConfigConst.mail_smtp_pw = getStringProperty(MAIL_SMTP_PW, ConfigConst.mail_smtp_pw);
		ConfigConst.mail_from = getStringProperty(MAIL_FROM, ConfigConst.mail_from);
		ConfigConst.mail_to = getStringProperty(MAIL_TO, ConfigConst.mail_to);
		ConfigConst.mail_cc = getStringProperty(MAIL_CC, ConfigConst.mail_cc);
		ConfigConst.mail_bcc = getStringProperty(MAIL_BCC, ConfigConst.mail_bcc);
		ConfigConst.mail_xml = getBoolProperty(MAIL_XML, ConfigConst.mail_xml);
		ConfigConst.mail_gif = getBoolProperty(MAIL_GIF, ConfigConst.mail_gif);
		ConfigConst.mail_pdf = getBoolProperty(MAIL_PDF, ConfigConst.mail_pdf);

		/* Generate Class Element Options */
		ConfigConst.generateClassPackage = getBoolProperty(GENERATE_CLASS_PACKAGE, ConfigConst.generateClassPackage);
		ConfigConst.generateClassFields = FieldOptions.getEnum(getStringProperty(GENERATE_CLASS_FIELDS, ConfigConst.generateClassFields.toString()));
		ConfigConst.generateClassMethods = MethodOptions.getEnum(getStringProperty(GENERATE_CLASS_METHODS, ConfigConst.generateClassMethods.toString()));
		ConfigConst.generateClassSignatures = SignatureOptions.getEnum(getStringProperty(GENERATE_CLASS_SIGNATURES, ConfigConst.generateClassSignatures.toString()));
		ConfigConst.generateClassSortings = SortOptions.getEnum(getStringProperty(GENERATE_CLASS_SORTINGS, ConfigConst.generateClassSortings.toString()));
	}

	public static void saveConfig(BaseGUI gui) {
		Config cfg = Config.getInstance();

		if (configfile == null) {
			return;
		}
		try {
			configfile.delete();
			configfile.createNewFile();

			Properties props = new Properties();

			props.setProperty(PROGRAM_VERSION, Program.VERSION);
			props.setProperty(DEFAULT_FONTSIZE, Integer.toString(ConfigConst.defaultFontsize));
			props.setProperty(PROPERTIES_PANEL_FONTSIZE, Integer.toString(Constants.propertiesPanelFontsize));
			props.setProperty(DEFAULT_FONTFAMILY, ConfigConst.defaultFontFamily);
			props.setProperty(SHOW_STICKINGPOLYGON, Boolean.toString(SharedConstants.show_stickingpolygon));
			props.setProperty(SHOW_GRID, Boolean.toString(ConfigConst.show_grid));
			props.setProperty(ENABLE_CUSTOM_ELEMENTS, Boolean.toString(ConfigConst.enable_custom_elements));
			props.setProperty(UI_MANAGER, cfg.getUiManager());
			props.setProperty(PRINT_PADDING, Integer.toString(ConfigConst.printPadding));
			props.setProperty(PDF_EXPORT_FONT, ConfigConst.pdfExportFont);
			props.setProperty(CHECK_FOR_UPDATES, Boolean.toString(ConfigConst.checkForUpdates));
			props.setProperty(OPEN_FILE_HOME, cfg.getOpenFileHome());
			props.setProperty(SAVE_FILE_HOME, cfg.getSaveFileHome());
			props.setProperty(DEV_MODE, Boolean.toString(SharedConstants.dev_mode));
			props.setProperty(LAST_USED_PALETTE, ConfigConst.lastUsedPalette);

			props.setProperty(MAIN_SPLIT_POSITION, Integer.toString(gui.getMainSplitPosition()));
			props.setProperty(RIGHT_SPLIT_POSITION, Integer.toString(gui.getRightSplitPosition()));
			props.setProperty(MAIL_SPLIT_POSITION, Integer.toString(gui.getMailSplitPosition()));
			if (gui instanceof StandaloneGUI) {
				// If the window is maximized in any direction this fact is written in the cfg
				Frame topContainer = ((StandaloneGUI) gui).getMainFrame();
				if ((topContainer.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
					props.setProperty(START_MAXIMIZED, "true");
				}
				// Otherwise the size and the location is written in the cfg
				else {
					props.setProperty(START_MAXIMIZED, "false");
					props.setProperty(PROGRAM_SIZE, topContainer.getSize().width + "," + topContainer.getSize().height);
					props.setProperty(PROGRAM_LOCATION, topContainer.getLocation().x + "," + topContainer.getLocation().y);
				}
			}
			if (!RecentlyUsedFilesList.getInstance().isEmpty()) {
				String recentFileString = "";
				for (String recentFile : RecentlyUsedFilesList.getInstance()) {
					recentFileString += recentFile + "|";
				}
				props.setProperty(RECENT_FILES, recentFileString.substring(0, recentFileString.length() - 1));
			}

			/* MAIL */
			if (!!ConfigConst.mail_smtp.isEmpty()) {
				props.setProperty(MAIL_SMTP, ConfigConst.mail_smtp);
			}
			props.setProperty(MAIL_SMTP_AUTH, Boolean.toString(ConfigConst.mail_smtp_auth));
			if (!ConfigConst.mail_smtp_user.isEmpty()) {
				props.setProperty(MAIL_SMTP_USER, ConfigConst.mail_smtp_user);
			}
			props.setProperty(MAIL_SMTP_PW_STORE, Boolean.toString(ConfigConst.mail_smtp_pw_store));
			if (!ConfigConst.mail_smtp_pw.isEmpty()) {
				props.setProperty(MAIL_SMTP_PW, ConfigConst.mail_smtp_pw);
			}
			if (!ConfigConst.mail_from.isEmpty()) {
				props.setProperty(MAIL_FROM, ConfigConst.mail_from);
			}
			if (!ConfigConst.mail_to.isEmpty()) {
				props.setProperty(MAIL_TO, ConfigConst.mail_to);
			}
			if (!ConfigConst.mail_cc.isEmpty()) {
				props.setProperty(MAIL_CC, ConfigConst.mail_cc);
			}
			if (!ConfigConst.mail_bcc.isEmpty()) {
				props.setProperty(MAIL_BCC, ConfigConst.mail_bcc);
			}
			props.setProperty(MAIL_XML, Boolean.toString(ConfigConst.mail_xml));
			props.setProperty(MAIL_GIF, Boolean.toString(ConfigConst.mail_gif));
			props.setProperty(MAIL_PDF, Boolean.toString(ConfigConst.mail_pdf));

			/* Generate Class Element Options */
			props.setProperty(GENERATE_CLASS_PACKAGE, Boolean.toString(ConfigConst.generateClassPackage));
			props.setProperty(GENERATE_CLASS_FIELDS, ConfigConst.generateClassFields.toString());
			props.setProperty(GENERATE_CLASS_METHODS, ConfigConst.generateClassMethods.toString());
			props.setProperty(GENERATE_CLASS_SIGNATURES, ConfigConst.generateClassSignatures.toString());
			props.setProperty(GENERATE_CLASS_SORTINGS, ConfigConst.generateClassSortings.toString());

			FileOutputStream outStream = new FileOutputStream(configfile);
			try {
				props.store(outStream, null);
			} finally {
				outStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static int getIntProperty(String key, int defaultValue) {
		String result = props.getProperty(key);
		if (result != null) {
			try {
				return Integer.parseInt(result);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	private static boolean getBoolProperty(String key, boolean defaultValue) {
		String result = props.getProperty(key);
		if (result != null) {
			return Boolean.parseBoolean(result);
		}
		return defaultValue;
	}

	private static String getStringProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	private static Dimension getDimensionProperty(String key, Dimension defaultValue) {
		String result = props.getProperty(key);
		if (result != null) {
			try {
				int x = Integer.parseInt(result.substring(0, result.indexOf(",")));
				int y = Integer.parseInt(result.substring(result.indexOf(",") + 1));
				return new Dimension(x, y);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	private static Point getPointProperty(String key, Point defaultValue) {
		String result = props.getProperty(key);
		if (result != null) {
			try {
				int x = Integer.parseInt(result.substring(0, result.indexOf(",")));
				int y = Integer.parseInt(result.substring(result.indexOf(",") + 1));
				return new Point(x, y);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
}