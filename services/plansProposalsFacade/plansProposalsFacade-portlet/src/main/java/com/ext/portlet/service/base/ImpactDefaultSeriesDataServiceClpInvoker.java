package com.ext.portlet.service.base;

import com.ext.portlet.service.ImpactDefaultSeriesDataServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ImpactDefaultSeriesDataServiceClpInvoker {
    private String _methodName476;
    private String[] _methodParameterTypes476;
    private String _methodName477;
    private String[] _methodParameterTypes477;

    public ImpactDefaultSeriesDataServiceClpInvoker() {
        _methodName476 = "getBeanIdentifier";

        _methodParameterTypes476 = new String[] {  };

        _methodName477 = "setBeanIdentifier";

        _methodParameterTypes477 = new String[] { "java.lang.String" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName476.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes476, parameterTypes)) {
            return ImpactDefaultSeriesDataServiceUtil.getBeanIdentifier();
        }

        if (_methodName477.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes477, parameterTypes)) {
            ImpactDefaultSeriesDataServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        throw new UnsupportedOperationException();
    }
}
