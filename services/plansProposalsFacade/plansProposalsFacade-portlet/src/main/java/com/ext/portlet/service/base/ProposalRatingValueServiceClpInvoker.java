package com.ext.portlet.service.base;

import com.ext.portlet.service.ProposalRatingValueServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class ProposalRatingValueServiceClpInvoker {
    private String _methodName548;
    private String[] _methodParameterTypes548;
    private String _methodName549;
    private String[] _methodParameterTypes549;

    public ProposalRatingValueServiceClpInvoker() {
        _methodName548 = "getBeanIdentifier";

        _methodParameterTypes548 = new String[] {  };

        _methodName549 = "setBeanIdentifier";

        _methodParameterTypes549 = new String[] { "java.lang.String" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName548.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes548, parameterTypes)) {
            return ProposalRatingValueServiceUtil.getBeanIdentifier();
        }

        if (_methodName549.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes549, parameterTypes)) {
            ProposalRatingValueServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        throw new UnsupportedOperationException();
    }
}
