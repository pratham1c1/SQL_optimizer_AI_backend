package com.AiProject.SqlOptimizer.service;

import com.AiProject.SqlOptimizer.dto.MetadataExtraction;
import com.AiProject.SqlOptimizer.exceptions.InvalidSqlException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    public MetadataExtraction extractMetadata(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableNames = tablesNamesFinder.getTableList(statement);

            List<String> joinsList = new ArrayList<>();
            List<String> whereClauses = new ArrayList<>();

            if (statement instanceof Select selectStatement) {
                if (selectStatement.getSelectBody() instanceof PlainSelect plainSelect) {
                    if (plainSelect.getJoins() != null) {
                        plainSelect.getJoins().forEach(join -> joinsList.add(join.toString()));
                    }
                    if (plainSelect.getWhere() != null) {
                        whereClauses.add(plainSelect.getWhere().toString());
                    }
                }
            }

            return MetadataExtraction.builder()
                    .isValid(true)
                    .queryType(statement.getClass().getSimpleName())
                    .tables(tableNames)
                    .joinConditions(joinsList)
                    .whereClauses(whereClauses)
                    .build();

        } catch (Exception e) {
            throw new InvalidSqlException("JSqlParser failed validation validation checking: " + e.getMessage());
        }
    }
}
