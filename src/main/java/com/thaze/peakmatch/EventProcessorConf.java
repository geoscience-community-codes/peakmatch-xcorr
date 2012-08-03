package com.thaze.peakmatch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.thaze.peakmatch.event.EventException;

public class EventProcessorConf {
	
	final Builder _builder;
	
	public static Builder buildFromConf (String confFile) throws EventException {
		
		Properties props = getProps(confFile);
		
		Builder b = new Builder();
		
		File dataset = new File(props.getProperty(				"dataset.full"));
		if (!dataset.exists() || !dataset.isDirectory() || dataset.listFiles().length == 0)
			throw new EventException("non-existent or empty dataset directory " + dataset);
		b.setDataset(dataset);
		
		File sampledataset = new File(props.getProperty(		"dataset.sample"));
		if (!sampledataset.exists() || !sampledataset.isDirectory() || sampledataset.listFiles().length == 0)
			throw new EventException("non-existent or empty sample dataset directory " + sampledataset);
		b.setSampledataset(sampledataset);
		
		b.setTopKPeaksToMatch(getInt(props, 					"top-k-peaks"));
		b.setSamplingStride(getInt(props, 						"sampling-stride"));
		b.setTopAmplitudeThreshold(getDouble(props, 			"top-amplitude-threshold"));
		b.setCandidateThreshold(getDouble(props, 				"candidate-threshold"));
		b.setFinalThreshold(getDouble(props,					"final-threshold"));
		b.setExpectedFileLineCount(getInt(props,				"expected-file-line-count"));
		
		try{
			b.setMode(Mode.valueOf(props.getProperty(			"mode")));
		} catch (IllegalArgumentException e){
			throw new EventException("invalid mode value '" + props.getProperty("mode") + "'");
		}
		b.setVerbose(Boolean.parseBoolean(props.getProperty(	"verbose")));
		b.setThreads(Integer.parseInt(props.getProperty(		"threads")));
		
		return b;
	}
	
	static double getDouble(Properties props, String key) throws EventException{
		String s = props.getProperty(key);
		if (null == s)
			throw new EventException("missing conf value: " + key);
		try{
			return Double.parseDouble(s);
		} catch (NumberFormatException e){
			throw new EventException("invalid double value for key " + key + ": " + s);
		}
	}
	static int getInt(Properties props, String key) throws EventException{
		String s = props.getProperty(key);
		if (null == s)
			throw new EventException("missing conf value: " + key);
		try{
			return Integer.parseInt(s);
		} catch (NumberFormatException e){
			throw new EventException("invalid int value for key " + key + ": " + s);
		}
	}
	
//	public static Builder newBuilder(){
//		return new Builder();
//	}
	
	private EventProcessorConf(Builder builder){
		_builder=builder;
	}
	
	public static enum Mode{ANALYSE, PEAKMATCH, FFTPRECACHE, POSTPROCESS}
	
	public static class Builder{
		private File dataset;
		private File sampledataset;
		private int TopKPeaksToMatch;
		private int samplingStride;
		private double topAmplitudeThreshold;
		private double candidateThreshold;
		private double finalThreshold;
		private int expectedFileLineCount;
		private Mode mode;
		private boolean verbose;
		private int threads;
		
		boolean _built;
		
		public EventProcessorConf build(){
			_built=true;
			return new EventProcessorConf(this);
		}
		
		private void assertState(){
			if (_built)
				throw new IllegalStateException("already built");
		}
		
		public File getDataset() {
			return dataset;
		}
		public Builder setDataset(File dataset) {
			assertState();
			this.dataset = dataset;
			return this;
		}
		public File getSampledataset() {
			return sampledataset;
		}
		public Builder setSampledataset(File sampledataset) {
			assertState();
			this.sampledataset = sampledataset;
			return this;
		}
		public int getTopKPeaksToMatch() {
			return TopKPeaksToMatch;
		}
		public Builder setTopKPeaksToMatch(int TopKPeaksToMatch) {
			assertState();
			this.TopKPeaksToMatch = TopKPeaksToMatch;
			return this;
		}
		public int getSamplingStride() {
			return samplingStride;
		}
		public Builder setSamplingStride(int samplingStride) {
			assertState();
			this.samplingStride = samplingStride;
			return this;
		}
		public double getTopAmplitudeThreshold() {
			return topAmplitudeThreshold;
		}
		public Builder setTopAmplitudeThreshold(double topAmplitudeThreshold) {
			assertState();
			this.topAmplitudeThreshold = topAmplitudeThreshold;
			return this;
		}
		public double getCandidateThreshold() {
			return candidateThreshold;
		}
		public Builder setCandidateThreshold(double candidateThreshold) {
			assertState();
			this.candidateThreshold = candidateThreshold;
			return this;
		}

		public int getExpectedFileLineCount() {
			return expectedFileLineCount;
		}

		public Builder setExpectedFileLineCount(int expectedFileLineCount) {
			assertState();
			this.expectedFileLineCount = expectedFileLineCount;
			return this;
		}

		@Override
		public String toString() {
			return "\tdataset: \t\t" + dataset + "\n"
					+ "\tsampledataset: \t\t" + sampledataset + "\n"
					+ "\tTopKPeaksToMatch: \t" + TopKPeaksToMatch + "\n"
					+ "\tsamplingStride: \t" + samplingStride + "\n"
					+ "\ttopAmplitudeThreshold: \t" + topAmplitudeThreshold + "\n"
					+ "\tcandidateThreshold: \t" + candidateThreshold + "\n"
					+ "\tfinalThreshold: \t" + finalThreshold + "\n"
					+ "\texpectedFileLineCount: \t" + expectedFileLineCount + "\n" 
					+ "\tmode: \t\t\t" + mode + "\n"
					+ "\tverbose: \t\t" + verbose + "\n"
					+ "\tthreads: \t\t" + threads + "\n"
					;
		}

		public double getFinalThreshold() {
			return finalThreshold;
		}

		public Builder setFinalThreshold(double finalThreshold) {
			assertState();
			this.finalThreshold = finalThreshold;
			return this;
		}

		public Mode getMode() {
			return mode;
		}

		public Builder setMode(Mode mode) {
			assertState();
			this.mode = mode;
			return this;
		}

		public boolean isVerbose() {
			return verbose;
		}

		public Builder setVerbose(boolean verbose) {
			assertState();
			this.verbose = verbose;
			return this;
		}

		public int getThreads() {
			return threads;
		}

		public Builder setThreads(int threads) {
			assertState();
			this.threads = threads;
			return this;
		}
	}

	public File getDataset() {
		return _builder.getDataset();
	}

	public File getSampledataset() {
		return _builder.getSampledataset();
	}

	public int getTopKPeaksToMatch() {
		return _builder.getTopKPeaksToMatch();
	}

	public int getSamplingStride() {
		return _builder.getSamplingStride();
	}

	public double getTopAmplitudeThreshold() {
		return _builder.getTopAmplitudeThreshold();
	}

	public double getCandidateThreshold() {
		return _builder.getCandidateThreshold();
	}
	public double getFinalThreshold() {
		return _builder.getFinalThreshold();
	}
	public int getExpectedFileLineCount() {
		return _builder.getExpectedFileLineCount();
	}
	public boolean isVerbose() {
		return _builder.isVerbose();
	}
	public Mode getMode() {
		return _builder.getMode();
	}
	public int getThreads(){
		return _builder.getThreads();
	}
	
	public int countAllEvents() {
		return getDataset().listFiles().length;
	}
	
	@Override
	public String toString() {
		return _builder.toString();
	}
	
	private static Properties getProps(String filename) throws EventException{

		File propsFile = new File(filename);
		if (!propsFile.exists())
			throw new EventException("missing conf file " + filename);
		
		Properties props = new Properties();
		try {
			props.load(new FileReader(propsFile));
		} catch (IOException e) {
			throw new EventException(e);
		}
		return props;
	}
}
