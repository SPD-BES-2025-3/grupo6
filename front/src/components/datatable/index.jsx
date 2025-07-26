import React from "react";

import { Typography, Grid } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { ptBR } from "@mui/x-data-grid/locales";

import { convertedArray } from "@/helpers/format";

export const Table = ({ dataTable, columns, id, key }) => {
    const [rows, setRows] = React.useState(false);

    React.useEffect(() => {
        if (!!dataTable) {
            const rows = convertedArray(dataTable, id);
            setRows(rows);
        }
    }, [dataTable]);

    return (
        !!rows && (
            <DataGrid
                key={key}
                getRowId={(row) => row[id]}
                columns={columns}
                autosizeOnMount
                slots={{
                    toolbar: GridToolbar,
                }}
                slotProps={{
                    toolbar: {
                        // override default props
                        showQuickFilter: true,
                        csvOptions: { disableToolbarButton: true },
                        printOptions: { disableToolbarButton: true },
                    },
                }}
                initialState={{
                    sorting: {
                        sortModel: [{ field: id, sort: "desc" }],
                    },
                }}
                localeText={ptBR.components.MuiDataGrid.defaultProps.localeText}
                rows={rows}
            />
        )
    );
};

const TableQuery = ({ dataTable, columns, id }) => {
    return (
        <Grid container>
            {dataTable.length === 0 ? (
                <Grid size={{ xs: 12 }} display="flex" justifyContent="center" alignItems="center" mt={3} sx={{ minHeight: "60vh" }}>
                    <Typography variant="h6">Houve um erro ao carregar os dados da tabela!</Typography>
                </Grid>
            ) : (
                <Table columns={columns} dataTable={dataTable?.data} id={id} />
            )}
        </Grid>
    );
};

export default TableQuery;
