package edu.illinois.ncsa.versus.store;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Properties;

import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;

public class DiskIndexProcessor implements IndexProcessor {

	String folder;

	DiskIndexProcessor() {
		Properties properties;
		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.folder");
			if (location != null) {
				folder = location;
				// log.debug("directory="+directory);
				System.out.println("indexfolder=" + folder);
			} else {
				folder = System.getProperty("java.io.tmpdir");
				System.out.println("indexfolder=" + folder);
			}
			// System.out.println("Storing file in " + directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addIndex(Index in) {

		File indexfile = new File(folder);

		try {
			write(indexfile, in);
			System.out.println("Index written to "
					+ indexfile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Index getIndex(String id) {
		FileInputStream in;
		Index index;
		ArrayList<Index> indexes = new ArrayList<Index>();
		try {
			in = new FileInputStream(new File(folder));

			while (true) {
				try {
					indexes.add((Index) read(in));
				} catch (EOFException e) {
					in.close();
					for (Index i : indexes) {
						if (i.getId().equals(id)) {
							return i;
						}
					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Collection<Index> listAll() {
		ArrayList<Index> indexes = new ArrayList<Index>();
				
		Index index;
		
		File indexfile = new File(folder);
        if(!indexfile.exists()){
			return indexes;
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(indexfile);

			while (true) {
				try {
					indexes.add((Index) read(fis));
					
				} catch (EOFException e) {
					fis.close();
				  return indexes;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return indexes;
	}

	private static void write(File indexFile, Index in) throws IOException {
		ObjectOutputStream oos = getOOS(indexFile);
		oos.writeObject(in);
		oos.flush();
		oos.close();
	}

	
	private static Index read(FileInputStream fis)
			throws ClassNotFoundException, IOException {
		Index index = (Index) getOIS(fis).readObject();
		return index;
	}
	
	private static ObjectOutputStream getOOS(File indexFile) throws IOException {
		if (indexFile.exists()) {
			// this is a workaround so that we can append objects to an existing
			// file
			return new AppendableObjectOutputStream(new FileOutputStream(
					indexFile, true));
		} else {
			return new ObjectOutputStream(new FileOutputStream(indexFile));
		}
	}

	private static ObjectInputStream getOIS(FileInputStream fis)
			throws IOException {
		long pos = fis.getChannel().position();
		return pos == 0 ? new ObjectInputStream(fis)
				: new AppendableObjectInputStream(fis);
	}

	private static class AppendableObjectOutputStream extends
			ObjectOutputStream {

		public AppendableObjectOutputStream(OutputStream out)
				throws IOException {
			super(out);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// do not write a header
		}
	}

	private static class AppendableObjectInputStream extends ObjectInputStream {

		public AppendableObjectInputStream(InputStream in) throws IOException {
			super(in);
		}

		@Override
		protected void readStreamHeader() throws IOException {
			// do not read a header
		}
	}

}
