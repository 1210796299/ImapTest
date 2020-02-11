package com.hh.imap;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.james.imap.encode.main.DefaultImapEncoderFactory;
import org.apache.james.imap.main.DefaultImapDecoderFactory;
import org.apache.james.imapserver.netty.IMAPServerFactory;
import org.apache.james.metrics.logger.DefaultMetricFactory;
import org.apache.james.server.core.configuration.Configuration;
import org.apache.james.server.core.configuration.FileConfigurationProvider;
import org.apache.james.server.core.filesystem.FileSystemImpl;
import org.apache.velocity.texen.util.FileUtil;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.File;

/**
 * @author oyx
 * @date 2020-01-13 14:26
 */

public class HHIMAPServer {
	private HHSystem system;
	private HierarchicalConfiguration<ImmutableNode> configuration;
	private DefaultMetricFactory metricFactory;
	private FileSystemImpl fileSystem;

	public static void main(String[] args) throws Exception {
		HHIMAPServer server = new HHIMAPServer();
		server.start();

	}


	private void start() throws Exception {
		IMAPServerFactory imapServerFactory = initImapServerFactory();
		imapServerFactory.init();
	}

	private IMAPServerFactory initImapServerFactory() throws Exception {
		initSystem();
		IMAPServerFactory serverFactory = new IMAPServerFactory(fileSystem, new DefaultImapDecoderFactory().buildImapDecoder(),
				new DefaultImapEncoderFactory().buildImapEncoder(), system.getDefaultImapProcessorFactory(), new DefaultMetricFactory(), new HashedWheelTimer());
		serverFactory.configure(configuration);
		return serverFactory;
	}

	private void initSystem() throws Exception {
		system = new HHSystem();
//		String config = FileUtil.file(".", "config").getCanonicalPath();
		//配置文件目录
		Configuration c = Configuration.builder()
				.workingDirectory("")
				.configurationFromClasspath()
				.build();
		fileSystem = new FileSystemImpl(c.directories());
		configuration = new FileConfigurationProvider(fileSystem, c).getConfiguration("imapserver");
		system.setConfiguration(configuration);
		system.init();
	}

}
