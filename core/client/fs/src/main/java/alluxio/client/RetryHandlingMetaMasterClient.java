/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.client;

import alluxio.AbstractMasterClient;
import alluxio.Constants;
import alluxio.master.MasterClientConfig;
import alluxio.thrift.AlluxioService;
import alluxio.thrift.GetMasterInfoTOptions;
import alluxio.thrift.MetaMasterClientService;
import alluxio.wire.MasterInfo;
import alluxio.wire.MasterInfo.MasterInfoField;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A wrapper for the thrift client to interact with the meta master.
 *
 * Since thrift clients are not thread safe, this class is a wrapper to provide thread safety and
 * support for retries.
 */
@ThreadSafe
public final class RetryHandlingMetaMasterClient extends AbstractMasterClient
    implements MetaMasterClient {
  private MetaMasterClientService.Client mClient;

  /**
   * Creates a new meta master client.
   *
   * @param conf master client configuration
   */
  public RetryHandlingMetaMasterClient(MasterClientConfig conf) {
    super(conf);
    mClient = null;
  }

  @Override
  protected AlluxioService.Client getClient() {
    return mClient;
  }

  @Override
  protected String getServiceName() {
    return Constants.META_MASTER_SERVICE_NAME;
  }

  @Override
  protected long getServiceVersion() {
    return Constants.META_MASTER_CLIENT_SERVICE_VERSION;
  }

  @Override
  protected void afterConnect() {
    mClient = new MetaMasterClientService.Client(mProtocol);
  }

  @Override
  public synchronized MasterInfo getInfo(final Set<MasterInfoField> fields) throws IOException {
    return retryRPC(() -> {
      Set<alluxio.thrift.MasterInfoField> thriftFields = new HashSet<>();
      if (fields == null) {
        thriftFields = null;
      } else {
        for (MasterInfoField field : fields) {
          thriftFields.add(field.toThrift());
        }
      }
      return MasterInfo.fromThrift(
          mClient.getMasterInfo(new GetMasterInfoTOptions(thriftFields)).getMasterInfo());
    });
  }
}
