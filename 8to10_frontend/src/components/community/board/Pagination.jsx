import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import {PaginationItem} from "@mui/material";

import "@/styles/community/Board.css";

import PropTypes from "prop-types";

const PaginationComponent = ({ boardState, setBoardField }) => {

    return (
        <Stack spacing={2} alignItems="center">
            <Pagination
                count={boardState.totalPages + 1}
                page={boardState.pageNum}
                variant="outlined"
                shape="rounded"
                size="medium"
                renderItem={(item) => (
                    <PaginationItem
                        {...item}
                        onClick={() => {
                            if (item.type === 'previous' && boardState.pageNum > 1) {
                                setBoardField('pageNum', boardState.pageNum - 1);
                            }
                            if (item.type === 'next' && boardState.pageNum < boardState.totalPages) {
                                setBoardField('pageNum', boardState.pageNum + 1);
                            }
                            if (item.type === 'page') {
                                setBoardField('pageNum', item.page);
                            }
                            window.scrollTo({
                                top: 0,
                                behavior: 'smooth'
                            });
                        }}
                    />
                )}
            />
        </Stack>
    );
};

PaginationComponent.propTypes = {
    boardState: PropTypes.shape({
        sortCondition: PropTypes.string,
        searchCondition: PropTypes.string,
        searchKeyword: PropTypes.string,
        postsPerPage: PropTypes.number,
        totalPages: PropTypes.number.isRequired,
        pageNum: PropTypes.number.isRequired,
    }).isRequired,
    setBoardField: PropTypes.func.isRequired,
};

export default PaginationComponent;
