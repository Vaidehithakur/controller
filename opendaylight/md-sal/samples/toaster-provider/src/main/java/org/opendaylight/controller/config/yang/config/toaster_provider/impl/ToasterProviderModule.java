/**
* Generated file

* Generated from: yang module name: toaster-provider-impl  yang module local name: toaster-provider-impl
* Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
* Generated at: Wed Feb 05 11:05:32 CET 2014
*
* Do not modify this file unless it is present under src/main directory
*/
package org.opendaylight.controller.config.yang.config.toaster_provider.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sal.binding.api.data.DataChangeListener;
import org.opendaylight.controller.sal.binding.api.data.DataProviderService;
import org.opendaylight.controller.sample.toaster.provider.OpendaylightToaster;
import org.opendaylight.yang.gen.v1.http.netconfcentral.org.ns.toaster.rev091120.ToasterService;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
*/
public final class ToasterProviderModule extends org.opendaylight.controller.config.yang.config.toaster_provider.impl.AbstractToasterProviderModule
 {
    private static final Logger log = LoggerFactory.getLogger(ToasterProviderModule.class);

    public ToasterProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public ToasterProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
            ToasterProviderModule oldModule, java.lang.AutoCloseable oldInstance) {

        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    protected void customValidation() {
        // No need to validate dependencies, since all dependencies have mandatory true flag in yang
        // config-subsystem will perform the validation for dependencies
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final OpendaylightToaster opendaylightToaster = new OpendaylightToaster();

        // Register to md-sal
        opendaylightToaster.setNotificationProvider(getNotificationServiceDependency());

        DataProviderService dataBrokerService = getDataBrokerDependency();
        opendaylightToaster.setDataProvider(dataBrokerService);

        final ListenerRegistration<DataChangeListener> dataChangeListenerRegistration =
                dataBrokerService.registerDataChangeListener( OpendaylightToaster.TOASTER_IID, opendaylightToaster );

        final BindingAwareBroker.RpcRegistration<ToasterService> rpcRegistration = getRpcRegistryDependency()
                .addRpcImplementation(ToasterService.class, opendaylightToaster);

        // Register runtimeBean for toaster statistics via JMX
        final ToasterProviderRuntimeRegistration runtimeReg = getRootRuntimeBeanRegistratorWrapper().register(
                opendaylightToaster);

        // Wrap toaster as AutoCloseable and close registrations to md-sal at
        // close()
        final class AutoCloseableToaster implements AutoCloseable {

            @Override
            public void close() throws Exception {
                dataChangeListenerRegistration.close();
                rpcRegistration.close();
                runtimeReg.close();
                opendaylightToaster.close();
                log.info("Toaster provider (instance {}) torn down.", this);
            }
        }

        AutoCloseable ret = new AutoCloseableToaster();
        log.info("Toaster provider (instance {}) initialized.", ret);
        return ret;
    }
}
