/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Simon Kaegi, Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.server.bespin;

import javax.servlet.ServletException;
import org.osgi.framework.*;
import org.osgi.service.http.*;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private ServiceTracker httpServiceTracker;
    protected static volatile BundleContext bundleContext;

    public void start(BundleContext context) throws Exception {
        bundleContext = context;
        httpServiceTracker = new HttpServiceTracker(context);
        httpServiceTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        httpServiceTracker.close();
        httpServiceTracker = null;
        bundleContext = null;
    }

    private class HttpServiceTracker extends ServiceTracker {

        public HttpServiceTracker(BundleContext context) {
            super(context, HttpService.class.getName(), null);
        }

        public Object addingService(ServiceReference reference) {
            HttpService httpService = (HttpService) super.addingService(reference); // calls context.getService(reference);
            if (httpService == null)
                return null;

            HttpContext httpContext = new BundleEntryHttpContext(context.getBundle());
            Bundle bespinBundle = getBespinBundle();
            HttpContext bespinHttpContext = new BundleEntryHttpContext(bespinBundle);

            try {
                httpService.registerResources("/", "/frontend", bespinHttpContext);
                httpService.registerResources("/eclipse/commands", "/commands", httpContext);
                httpService.registerServlet("/register", new AuthenticationServlet(), null, httpContext);
                httpService.registerServlet("/edit", new EditingServlet(), null, httpContext);
                httpService.registerServlet("/file", new FilesServlet(), null, httpContext);
                httpService.registerServlet("/markers", new MarkersServlet(), null, httpContext);
                httpService.registerServlet("/cvs", new CVSServlet(), null, httpContext);
                httpService.registerServlet("/project", new ProjectsServlet(), null, httpContext);
                httpService.registerServlet("/settings", new SettingsServlet(), null, httpContext);
            } catch (NamespaceException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return httpService;
        }

        private Bundle getBespinBundle() {
            Bundle[] bundles = context.getBundles();
            for (int i = 0; i < bundles.length; i++) {
                if (bundles[i].getSymbolicName().equals("bespin"))
                    return bundles[i];
            }
            throw new IllegalStateException("Couldn't find the 'bespin' bundle.");
        }

        public void removedService(ServiceReference reference, Object service) {
            HttpService httpService = (HttpService) service;
            httpService.unregister("/");
            httpService.unregister("/register");
            httpService.unregister("/edit");
            httpService.unregister("/file");
            httpService.unregister("/markers");
            httpService.unregister("/cvs");
            httpService.unregister("/project");
            httpService.unregister("/settings");

            super.removedService(reference, service); // calls context.ungetService(reference);
        }
    }
}
