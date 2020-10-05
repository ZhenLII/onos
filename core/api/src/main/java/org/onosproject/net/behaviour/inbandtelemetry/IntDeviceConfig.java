/*
 * Copyright 2015-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.net.behaviour.inbandtelemetry;

import com.google.common.annotations.Beta;
import org.onlab.packet.IpAddress;
import org.onlab.packet.MacAddress;
import org.onlab.packet.TpPort;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Device-level configuration of the INT process.
 */
@Beta
public final class IntDeviceConfig {
    /**
     * Represents a type of telemetry spec to collect in the dataplane.
     */
    public enum TelemetrySpec {
        /**
         * Embeds telemetry metadata according to the INT specification.
         *
         * @see <a href="https://github.com/p4lang/p4-applications/blob/master/docs/INT.pdf">
         *     INT sepcification</a>
         */
        INT,
        /**
         * Embeds telemetry metadata according to the OAM specification.
         *
         * @see <a href="https://tools.ietf.org/html/draft-ietf-ippm-ioam-data">
         *     Data fields for In-situ OAM</a>
         */
        IOAM
    }

    private final IpAddress collectorIp;
    private final TpPort collectorPort;
    private final MacAddress collectorNextHopMac;
    private final IpAddress sinkIp;
    private final MacAddress sinkMac;
    private final TelemetrySpec spec;
    private boolean enabled;
    private int hopLatencySensitivity;

    private IntDeviceConfig(IpAddress collectorIp, TpPort collectorPort, MacAddress collectorNextHopMac,
                            IpAddress sinkIp, MacAddress sinkMac, TelemetrySpec spec, boolean enabled,
                            int hopLatencySensitivity) {
        this.collectorIp = collectorIp;
        this.collectorPort = collectorPort;
        this.collectorNextHopMac = collectorNextHopMac;
        this.sinkIp = sinkIp;
        this.sinkMac = sinkMac;
        this.spec = spec;
        this.enabled = enabled;
        this.hopLatencySensitivity = hopLatencySensitivity;
    }

    /**
     * Returns IP address of the collector.
     * This is the destination IP address that will be used for all INT reports
     * generated by all sink devices.
     *
     * @return collector IP address
     */
    public IpAddress collectorIp() {
        return collectorIp;
    }

    /**
     * Returns UDP port number of the collector.
     * This is the destination UDP port number that will be used for all INT reports
     * generated by all sink devices.
     *
     * @return collector UDP port number
     */
    public TpPort collectorPort() {
        return collectorPort;
    }

    /**
     * Returns MAC address of next hop of INT report packets.
     * This can be either MAC address of the collector or a router.
     * This is an optional parameter, which means that the usage of this
     * parameter depends on IntProgrammable implementation.
     * (e.g., If a report packet needs to be routed to reach the collector,
     * IntProgrammable will ignore this value and choose next hop router's MAC address.
     * If a collector itself is the next hop of INT report packets, then
     * this value will be used as a destination MAC address for all INT report packets.)
     *
     * @return MAC address of next hop of INT report packets
     */
    public MacAddress collectorNextHopMac() {
        return collectorNextHopMac;
    }

    /**
     * Returns IP address of the sink device.
     * All sink devices share this address as the source IP address
     * for all INT reports.
     *
     * @return sink device's IP address
     */
    public IpAddress sinkIp() {
        return sinkIp;
    }

    /**
     * Returns MAC address of the sink device.
     * All sink devices share this address as the source MAC address
     * for all INT reports.
     *
     * @return sink device's MAC address
     */
    public MacAddress sinkMac() {
        return sinkMac;
    }

    /**
     * Returns the type of telemetry spec as per {@link TelemetrySpec}.
     *
     * @return telemetry spec
     */
    public TelemetrySpec spec() {
        return spec;
    }

    /**
     * Returns the status of INT functionality.
     *
     * @return true if INT is enabled; false otherwise.
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     * Returns the minimal interval of hop latency change for a flow.
     *
     * @return the minimal interval of hop latency change for a flow.
     */
    public int minFlowHopLatencyChangeNs() {
        return hopLatencySensitivity;
    }

    /**
     * Returns a new builder.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * An IntConfig object builder.
     */
    public static final class Builder {
        private IpAddress collectorIp = IpAddress.valueOf(0);
        private TpPort collectorPort = TpPort.tpPort(0);
        private MacAddress collectorNextHopMac = MacAddress.valueOf(0);
        private IpAddress sinkIp = IpAddress.valueOf(0);
        private MacAddress sinkMac = MacAddress.valueOf(0);
        private TelemetrySpec spec = TelemetrySpec.INT;
        private boolean enabled = false;
        int minFlowHopLatencyChangeNs = 0;

        /**
         * Assigns a collector IP address to the IntConfig object.
         *
         * @param collectorIp IP address of the collector
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withCollectorIp(IpAddress collectorIp) {
            this.collectorIp = collectorIp;
            return this;
        }

        /**
         * Assigns a collector UDP port to the IntConfig object.
         *
         * @param collectorPort UDP port number of the collector
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withCollectorPort(TpPort collectorPort) {
            this.collectorPort = collectorPort;
            return this;
        }

        /**
         * Assigns a MAC address of the next hop to the collector
         * to the IntConfig object.
         *
         * @param collectorNextHopMac MAC address of the collector
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withCollectorNextHopMac(MacAddress collectorNextHopMac) {
            this.collectorNextHopMac = collectorNextHopMac;
            return this;
        }

        /**
         * Assigns an IP address of the sink device to the IntConfig object.
         *
         * @param sinkIp sink device's IP address
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withSinkIp(IpAddress sinkIp) {
            this.sinkIp = sinkIp;
            return this;
        }

        /**
         * Assigns a MAC address of the sink device to the IntConfig object.
         *
         * @param sinkMac sink device's MAC address
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withSinkMac(MacAddress sinkMac) {
            this.sinkMac = sinkMac;
            return this;
        }

        /**
         * Assigns the type of telemetry spec to the IntConfig object.
         *
         * @param spec telemetry spec
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withTelemetrySpec(TelemetrySpec spec) {
            this.spec = spec;
            return this;
        }

        /**
         * Assigns the status of INT.
         * True to enable INT functionality, false otherwise.
         *
         * @param enabled the status of INT
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Sets the minimal interval of hop latency change for a flow.
         *
         * @param value the minimal interval of hop latency change for a flow
         * @return an IntConfig builder
         */
        public IntDeviceConfig.Builder withMinFlowHopLatencyChangeNs(int value) {
            this.minFlowHopLatencyChangeNs = value;
            return this;
        }

        /**
         * Builds the IntConfig object.
         *
         * @return an IntConfig object
         */
        public IntDeviceConfig build() {
            checkNotNull(collectorIp, "Collector IP should be specified.");
            checkNotNull(collectorPort, "Collector port number should be specified.");
            checkArgument(minFlowHopLatencyChangeNs >= 0, "Hop latency sensitivity must be positive or zero");

            return new IntDeviceConfig(collectorIp, collectorPort, collectorNextHopMac,
                                 sinkIp, sinkMac, spec, enabled, minFlowHopLatencyChangeNs);
        }
    }
}
