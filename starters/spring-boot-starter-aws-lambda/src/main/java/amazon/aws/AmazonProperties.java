package amazon.aws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a configuration properties model for authenticating with AWS.
 *
 * @author kbastani
 */
@Configuration
@ConfigurationProperties(prefix = "amazon")
public class AmazonProperties {

	@NestedConfigurationProperty
	private Aws aws;

	@NestedConfigurationProperty
	private Functions functions;

	/**
	 * A property group for Amazon Web Service (AWS) configurations
	 *
	 * @return a property group for AWS configurations
	 */
	public Aws getAws() {
		return aws;
	}

	/**
	 * A property group for Amazon Web Service (AWS) configurations
	 *
	 * @param aws is a property group for AWS configurations
	 */
	public void setAws(Aws aws) {
		this.aws = aws;
	}

	/**
	 * A property group for configuring AWS Lambda invocations
	 *
	 * @return a property group for configuring AWS Lambda invocations
	 */
	public Functions getFunctions() {
		return functions;
	}

	/**
	 * A property group for configuring AWS Lambda invocations
	 *
	 * @param functions is a property group for configuring AWS Lambda invocations
	 */
	public void setFunctions(Functions functions) {
		this.functions = functions;
	}

	/**
	 * A property group for enabling/disabling AWS functions
	 */
	public static class Functions {

		private Boolean enabled = true;

		/**
		 * Enables AWS Lambda function invocation. default: true
		 *
		 * @return a {@link Boolean} indicating whether or not to auto configure an AWS function invoker
		 */
		public Boolean getEnabled() {
			return enabled;
		}

		/**
		 * If enabled, a function invoker for AWS Lambda will be auto-configured
		 *
		 * @param enabled indicating whether or not to auto configure an AWS function invoker. default: true
		 */
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
	}

	/**
	 * A property group for Amazon Web Service (AWS) configurations
	 */
	public static class Aws {

		private String accessKeyId;
		private String accessKeySecret;

		/**
		 * A valid AWS account's access key id.
		 *
		 * @return an AWS access key id
		 */
		public String getAccessKeyId() {
			return accessKeyId;
		}

		/**
		 * A valid AWS account's access key id.
		 *
		 * @param accessKeyId is a valid AWS account's access key id.
		 */
		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		/**
		 * A valid AWS account's secret access token.
		 *
		 * @return an AWS account's secret access key
		 */
		public String getAccessKeySecret() {
			return accessKeySecret;
		}

		/**
		 * A valid AWS account's secret access token.
		 *
		 * @param accessKeySecret is a valid AWS account's secret access token.
		 */
		public void setAccessKeySecret(String accessKeySecret) {
			this.accessKeySecret = accessKeySecret;
		}

		@Override
		public String toString() {
			return "Aws{" +
				"accessKeyId='" + accessKeyId + '\'' +
				", accessKeySecret='" + accessKeySecret + '\'' +
				'}';
		}
	}
}
