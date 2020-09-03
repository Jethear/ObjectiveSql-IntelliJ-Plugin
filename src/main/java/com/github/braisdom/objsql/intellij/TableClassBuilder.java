package com.github.braisdom.objsql.intellij;

import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.impl.light.LightPsiClassBuilder;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.getProjectType;

final class TableClassBuilder {

    static void buildClass(PsiClass psiClass, List result) {
        List<PsiField> psiFields = new ArrayList<>();
        LightPsiClassBuilder classBuilder = new LightPsiClassBuilder(psiClass, "Table") {
            @NotNull
            @Override
            public PsiField[] getFields() {
                return psiFields.toArray(new PsiField[]{});
            }

            @Override
            public PsiFile getContainingFile() {
                return psiClass.getContainingFile();
            }
        };
        LightModifierList modifier = classBuilder.getModifierList();

        modifier.addModifier(PsiModifier.PUBLIC);
        modifier.addModifier(PsiModifier.STATIC);
        modifier.addModifier(PsiModifier.FINAL);
        classBuilder.setContainingClass(psiClass);
        classBuilder.setNavigationElement(psiClass);
        classBuilder.getExtendsList().addReference("com.github.braisdom.objsql.sql.AbstractTable");
        classBuilder.addMethod(createPrivateConstructor(psiClass, classBuilder));

        buildTableFields(psiClass, classBuilder, psiFields);

        result.add(classBuilder);
    }

    static void buildMethod(PsiClass psiClass, List result) {
        PsiClass[] innerClasses = psiClass.getInnerClasses();
        for (PsiClass innerClass : innerClasses) {
            if(innerClass.getName().equals("Table")) {
                ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "asTable");
                PsiType returnType = JavaPsiFacade.getElementFactory(innerClass.getProject()).createType(innerClass);
                methodBuilder.withMethodReturnType(returnType)
                        .withContainingClass(psiClass)
                        .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL);

                result.add(methodBuilder);
                break;
            }
        }
    }

    private static PsiMethod createPrivateConstructor(PsiClass psiClass, PsiClass innerClass) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), innerClass.getName());
        methodBuilder.withContainingClass(psiClass)
                .withMethodReturnType(PsiType.VOID)
                .withModifier(PsiModifier.PRIVATE)
                .setConstructor(true);
        return methodBuilder;
    }

    private static void buildTableFields(PsiClass psiClass, PsiClass innerClass, List<PsiField> psiFields) {
        Collection<PsiField> fields = PsiClassUtil.collectClassFieldsIntern(psiClass);
        for (PsiField field : fields) {
            PsiType fieldType = getProjectType("com.github.braisdom.objsql.sql.Column", psiClass.getProject());
            LightFieldBuilder fieldBuilder = new LightFieldBuilder(field.getName(), fieldType, psiClass);
            fieldBuilder.setModifiers(PsiModifier.PUBLIC, PsiModifier.FINAL);
            fieldBuilder.setContainingClass(innerClass);

            psiFields.add(fieldBuilder);
        }
    }
}
