package upm.app.presentation.view;

import lombok.extern.log4j.Log4j2;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class ReflectiveTableBuilder<T> {

    public static final String IS = "is";
    public static final String GET = "get";
    public static final String GET_CLASS = "getClass";
    public static final int HEADER_ROW_INDEX = 0;

    private final List<T> modelsInstances;

    public ReflectiveTableBuilder(List<T> modelsInstances) {
        this.modelsInstances = modelsInstances;
    }

    public Table buildShellTable() {
        String[][] tableData = (modelsInstances == null || modelsInstances.isEmpty())
                ? buildEmptyTableData()
                : buildModelsTableData();

        return new TableBuilder(new ArrayTableModel(tableData))
                .addFullBorder(BorderStyle.fancy_light)
                .build();
    }

    private String[][] buildEmptyTableData() {
        return new String[][]{{"Tabla vacía"}};
    }

    private String[][] buildModelsTableData() {
        Class<?> modelClass = modelsInstances.getFirst().getClass();
        List<Method> getters = collectGetterMethodsFromHierarchy(modelClass);
        return buildTableData(getters);
    }

    private List<Method> collectGetterMethodsFromHierarchy(Class<?> clazz) {
        List<Method> getters = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            this.addGetterMethods(current.getDeclaredMethods(), getters);
            current = current.getSuperclass();
        }
        getters.sort(Comparator.comparing(Method::getName));
        return getters;
    }

    private void addGetterMethods(Method[] clazz, List<Method> getters) {
        for (Method method : clazz) {
            if (isGetterMethod(method)) {
                getters.add(method);
            }
        }
    }

    private boolean isGetterMethod(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        boolean isGetClass = method.getName().equals(GET_CLASS);
        boolean isGetter = method.getName().startsWith(GET) && !method.getReturnType().equals(void.class);
        boolean isBooleanGetter = method.getName().startsWith(IS) &&
                (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class));
        return (method.getParameterCount() == 0) && !isGetClass && (isGetter || isBooleanGetter);
    }

    private String[][] buildTableData(List<Method> getters) {
        int numRows = modelsInstances.size() + 1;
        int numCols = getters.size();
        String[][] table = new String[numRows][numCols];

        for (int i = 0; i < getters.size(); i++) {
            String fieldName = extractFieldName(getters.get(i));
            table[HEADER_ROW_INDEX][i] = fieldName;
        }

        for (int rowIndex = 0; rowIndex < modelsInstances.size(); rowIndex++) {
            Object obj = modelsInstances.get(rowIndex);
            for (int colIndex = 0; colIndex < getters.size(); colIndex++) {
                table[rowIndex + 1][colIndex] = invokeGetter(obj, getters.get(colIndex));
            }
        }
        return table;
    }

    private String extractFieldName(Method method) {
        String name = method.getName().startsWith(IS)
                ? method.getName().substring(IS.length())
                : method.getName().substring(GET.length());
        return name.replaceAll("([a-z])([A-Z])", "$1 $2");
    }

    private String invokeGetter(Object targetObject, Method getter) {
        try {
            Object value = getter.invoke(targetObject);
            return value == null ? "null" : value.toString();
        } catch (Exception e) {
            log.warn("Error invoking getter {} on {}", getter.getName(), targetObject, e);
            return String.format("Error %s on %s", getter.getName(), targetObject);
        }
    }
}
