package processing.export;

import gui.dialogs.ExportDialog;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import locale.MessageSource;

import org.apache.commons.io.FileUtils;

import database.Database;

public class Exportation extends SwingWorker<Void, Integer>
{

	private List<String> files;
	private File output;
	private boolean openExplorer;
	private boolean exportAll;
	private JDialog jDialog;

	private boolean running = false;

	private static final String DATABASE_FILE = System.getProperty("user.name") + ".mnk";
	private static final String EXPORT_DATABASE_FILE = "export.mnk";

	private Exportation(Builder builder)
	{
		files = new ArrayList<String>();
		this.output = builder.output;
		this.openExplorer = builder.openExplorer;
		this.exportAll = builder.exportAll;
		this.jDialog = builder.jDialog;
	}

	private void generateFileList(File node) throws IOException
	{
		if (exportAll)
		{
			if (node.isFile())
			{
				if (!node.getName().equals(EXPORT_DATABASE_FILE))
					if (node.getName().equals(DATABASE_FILE))
					{
						File export = new File(Database.DEFAULT_DATABASE_FOLDER + File.separator + "export.mnk");
						FileUtils.copyFile(node, export);
						files.add(generateZipEntry(export.getAbsoluteFile().toString()));
					}
					else
						files.add(generateZipEntry(node.getAbsoluteFile().toString()));
			}

			if (node.isDirectory())
			{
				String[] subNote = node.list();
				for (String filename : subNote)
				{
					generateFileList(new File(node, filename));
				}
			}
		}
		else
		{
			File export = new File(Database.DEFAULT_DATABASE_FOLDER + File.separator + "export.mnk");
			FileUtils.copyFile(new File(Database.DEFAULT_FILE), export);
			files.add(generateZipEntry(export.getAbsoluteFile().toString()));
		}

	}

	private String generateZipEntry(String file)
	{
		File df = new File(Database.DEFAULT_DATABASE_FOLDER);
		return file.substring(df.getAbsoluteFile().toString().length() + 1, file.length());
	}

	@SuppressWarnings("resource")
	@Override
	protected Void doInBackground() throws Exception
	{
		setRunning(true);
		generateFileList(new File(Database.DEFAULT_DATABASE_FOLDER));
		byte[] buffer = new byte[1024];

		FileOutputStream fos = new FileOutputStream(output);
		ZipOutputStream zos = new ZipOutputStream(fos);

		setProgress(0);
		int total = files.size();
		for (int i = 1; i <= files.size(); i++)
		{
			if (!isCancelled())
			{
				String file = files.get(i - 1);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(Database.DEFAULT_DATABASE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0)
				{
					zos.write(buffer, 0, len);
				}

				in.close();
				setProgress((int) ((double) i / total * 100));
			}
			else
			{
				setProgress(0);
				return null;
			}
		}

		zos.closeEntry();
		// remember close it
		zos.close();

		File export = new File(Database.DEFAULT_DATABASE_FOLDER + File.separator + "export.mnk");
		export.delete();
		return null;
	}

	@Override
	protected void done()
	{
		if (!isCancelled() && openExplorer)
			try
			{
				Desktop.getDesktop().open(output.getParentFile());
			}
			catch (IOException e)
			{
			}
		else
			((ExportDialog) jDialog).getProgressBar().setValue(0);
		((ExportDialog) jDialog).enableComponents(jDialog.getContentPane(), true);
		setRunning(false);
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public static class Builder
	{
		private File output;
		private boolean openExplorer = false;
		private boolean exportAll = true;
		private JDialog jDialog = null;

		public Builder output(File output)
		{
			this.output = output;
			return this;
		}

		public Builder openExplorer(boolean openExplorer)
		{
			this.openExplorer = openExplorer;
			return this;
		}

		public Builder exportAll(boolean exportAll)
		{
			this.exportAll = exportAll;
			return this;
		}

		public Builder jDialog(JDialog jDialog)
		{
			this.jDialog = jDialog;
			return this;
		}

		public Exportation build()
		{
			return new Exportation(this);
		}
	}

}
