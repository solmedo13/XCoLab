package com.ext.portlet.service.base;

import com.ext.portlet.service.PointTypeServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class PointTypeServiceClpInvoker {
    private String _methodName502;
    private String[] _methodParameterTypes502;
    private String _methodName503;
    private String[] _methodParameterTypes503;

    public PointTypeServiceClpInvoker() {
        _methodName502 = "getBeanIdentifier";

        _methodParameterTypes502 = new String[] {  };

        _methodName503 = "setBeanIdentifier";

        _methodParameterTypes503 = new String[] { "java.lang.String" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName502.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes502, parameterTypes)) {
            return PointTypeServiceUtil.getBeanIdentifier();
        }

        if (_methodName503.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes503, parameterTypes)) {
            PointTypeServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        throw new UnsupportedOperationException();
    }
}
