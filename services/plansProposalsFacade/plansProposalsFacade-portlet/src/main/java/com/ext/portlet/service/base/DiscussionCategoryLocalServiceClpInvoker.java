package com.ext.portlet.service.base;

import com.ext.portlet.service.DiscussionCategoryLocalServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class DiscussionCategoryLocalServiceClpInvoker {
    private String _methodName0;
    private String[] _methodParameterTypes0;
    private String _methodName1;
    private String[] _methodParameterTypes1;
    private String _methodName2;
    private String[] _methodParameterTypes2;
    private String _methodName3;
    private String[] _methodParameterTypes3;
    private String _methodName4;
    private String[] _methodParameterTypes4;
    private String _methodName5;
    private String[] _methodParameterTypes5;
    private String _methodName6;
    private String[] _methodParameterTypes6;
    private String _methodName7;
    private String[] _methodParameterTypes7;
    private String _methodName8;
    private String[] _methodParameterTypes8;
    private String _methodName9;
    private String[] _methodParameterTypes9;
    private String _methodName10;
    private String[] _methodParameterTypes10;
    private String _methodName11;
    private String[] _methodParameterTypes11;
    private String _methodName12;
    private String[] _methodParameterTypes12;
    private String _methodName13;
    private String[] _methodParameterTypes13;
    private String _methodName14;
    private String[] _methodParameterTypes14;
    private String _methodName15;
    private String[] _methodParameterTypes15;
    private String _methodName564;
    private String[] _methodParameterTypes564;
    private String _methodName565;
    private String[] _methodParameterTypes565;
    private String _methodName570;
    private String[] _methodParameterTypes570;
    private String _methodName571;
    private String[] _methodParameterTypes571;
    private String _methodName572;
    private String[] _methodParameterTypes572;
    private String _methodName573;
    private String[] _methodParameterTypes573;
    private String _methodName574;
    private String[] _methodParameterTypes574;
    private String _methodName575;
    private String[] _methodParameterTypes575;
    private String _methodName576;
    private String[] _methodParameterTypes576;
    private String _methodName577;
    private String[] _methodParameterTypes577;
    private String _methodName578;
    private String[] _methodParameterTypes578;
    private String _methodName579;
    private String[] _methodParameterTypes579;
    private String _methodName580;
    private String[] _methodParameterTypes580;

    public DiscussionCategoryLocalServiceClpInvoker() {
        _methodName0 = "addDiscussionCategory";

        _methodParameterTypes0 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName1 = "createDiscussionCategory";

        _methodParameterTypes1 = new String[] { "long" };

        _methodName2 = "deleteDiscussionCategory";

        _methodParameterTypes2 = new String[] { "long" };

        _methodName3 = "deleteDiscussionCategory";

        _methodParameterTypes3 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName4 = "dynamicQuery";

        _methodParameterTypes4 = new String[] {  };

        _methodName5 = "dynamicQuery";

        _methodParameterTypes5 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery"
            };

        _methodName6 = "dynamicQuery";

        _methodParameterTypes6 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int"
            };

        _methodName7 = "dynamicQuery";

        _methodParameterTypes7 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int",
                "com.liferay.portal.kernel.util.OrderByComparator"
            };

        _methodName8 = "dynamicQueryCount";

        _methodParameterTypes8 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery"
            };

        _methodName9 = "dynamicQueryCount";

        _methodParameterTypes9 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery",
                "com.liferay.portal.kernel.dao.orm.Projection"
            };

        _methodName10 = "fetchDiscussionCategory";

        _methodParameterTypes10 = new String[] { "long" };

        _methodName11 = "getDiscussionCategory";

        _methodParameterTypes11 = new String[] { "long" };

        _methodName12 = "getPersistedModel";

        _methodParameterTypes12 = new String[] { "java.io.Serializable" };

        _methodName13 = "getDiscussionCategories";

        _methodParameterTypes13 = new String[] { "int", "int" };

        _methodName14 = "getDiscussionCategoriesCount";

        _methodParameterTypes14 = new String[] {  };

        _methodName15 = "updateDiscussionCategory";

        _methodParameterTypes15 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName564 = "getBeanIdentifier";

        _methodParameterTypes564 = new String[] {  };

        _methodName565 = "setBeanIdentifier";

        _methodParameterTypes565 = new String[] { "java.lang.String" };

        _methodName570 = "getCategoriesByCategoryGroupId";

        _methodParameterTypes570 = new String[] { "long" };

        _methodName571 = "getDiscussionCategoryById";

        _methodParameterTypes571 = new String[] { "long" };

        _methodName572 = "createDebateCategory";

        _methodParameterTypes572 = new String[] {
                "long", "java.lang.String", "java.lang.String",
                "com.liferay.portal.model.User"
            };

        _methodName573 = "getThreads";

        _methodParameterTypes573 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName574 = "addThread";

        _methodParameterTypes574 = new String[] {
                "com.ext.portlet.model.DiscussionCategory", "java.lang.String",
                "java.lang.String", "com.liferay.portal.model.User"
            };

        _methodName575 = "store";

        _methodParameterTypes575 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName576 = "getAuthor";

        _methodParameterTypes576 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName577 = "getLastActivityAuthor";

        _methodParameterTypes577 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName578 = "delete";

        _methodParameterTypes578 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };

        _methodName579 = "update";

        _methodParameterTypes579 = new String[] {
                "com.ext.portlet.model.DiscussionCategory", "java.lang.String",
                "java.lang.String"
            };

        _methodName580 = "getCategoryGroup";

        _methodParameterTypes580 = new String[] {
                "com.ext.portlet.model.DiscussionCategory"
            };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName0.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes0, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.addDiscussionCategory((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName1.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes1, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.createDiscussionCategory(((Long) arguments[0]).longValue());
        }

        if (_methodName2.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes2, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.deleteDiscussionCategory(((Long) arguments[0]).longValue());
        }

        if (_methodName3.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes3, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.deleteDiscussionCategory((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName4.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes4, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQuery();
        }

        if (_methodName5.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes5, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName6.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes6, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue());
        }

        if (_methodName7.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes7, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue(),
                (com.liferay.portal.kernel.util.OrderByComparator) arguments[3]);
        }

        if (_methodName8.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes8, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName9.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes9, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                (com.liferay.portal.kernel.dao.orm.Projection) arguments[1]);
        }

        if (_methodName10.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes10, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.fetchDiscussionCategory(((Long) arguments[0]).longValue());
        }

        if (_methodName11.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes11, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getDiscussionCategory(((Long) arguments[0]).longValue());
        }

        if (_methodName12.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes12, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getPersistedModel((java.io.Serializable) arguments[0]);
        }

        if (_methodName13.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes13, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getDiscussionCategories(((Integer) arguments[0]).intValue(),
                ((Integer) arguments[1]).intValue());
        }

        if (_methodName14.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes14, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getDiscussionCategoriesCount();
        }

        if (_methodName15.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes15, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.updateDiscussionCategory((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName564.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes564, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getBeanIdentifier();
        }

        if (_methodName565.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes565, parameterTypes)) {
            DiscussionCategoryLocalServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        if (_methodName570.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes570, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getCategoriesByCategoryGroupId(((Long) arguments[0]).longValue());
        }

        if (_methodName571.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes571, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getDiscussionCategoryById(((Long) arguments[0]).longValue());
        }

        if (_methodName572.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes572, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.createDebateCategory(((Long) arguments[0]).longValue(),
                (java.lang.String) arguments[1],
                (java.lang.String) arguments[2],
                (com.liferay.portal.model.User) arguments[3]);
        }

        if (_methodName573.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes573, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getThreads((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName574.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes574, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.addThread((com.ext.portlet.model.DiscussionCategory) arguments[0],
                (java.lang.String) arguments[1],
                (java.lang.String) arguments[2],
                (com.liferay.portal.model.User) arguments[3]);
        }

        if (_methodName575.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes575, parameterTypes)) {
            DiscussionCategoryLocalServiceUtil.store((com.ext.portlet.model.DiscussionCategory) arguments[0]);

            return null;
        }

        if (_methodName576.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes576, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getAuthor((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName577.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes577, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getLastActivityAuthor((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        if (_methodName578.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes578, parameterTypes)) {
            DiscussionCategoryLocalServiceUtil.delete((com.ext.portlet.model.DiscussionCategory) arguments[0]);

            return null;
        }

        if (_methodName579.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes579, parameterTypes)) {
            DiscussionCategoryLocalServiceUtil.update((com.ext.portlet.model.DiscussionCategory) arguments[0],
                (java.lang.String) arguments[1], (java.lang.String) arguments[2]);

            return null;
        }

        if (_methodName580.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes580, parameterTypes)) {
            return DiscussionCategoryLocalServiceUtil.getCategoryGroup((com.ext.portlet.model.DiscussionCategory) arguments[0]);
        }

        throw new UnsupportedOperationException();
    }
}
