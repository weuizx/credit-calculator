import styles from './Home.module.scss';
import Form from '../../Form/Form';
import PaymentTable from '../../Table/PaymentTable';
import сalculatorStore from '../../../stores/calculatorStore';
import { observer } from 'mobx-react-lite';
import ReactPaginate from 'react-paginate';
import { LINES_PER_PAGE } from '../../../constants';
import Button from '../../Button/Button';

const Home: React.FC = observer(() => {
  const { type, data, isError, currentPage, pagesCount, setCurrentPage, downloadFile } = сalculatorStore;

  const handlePageClick = (data: { selected: number }) => {
    setCurrentPage(data.selected + 1);
  };

  return (
    <div className={`${styles.container} container`}>
      <div className={styles.content}>
        <h1 className={styles['main-title']}>Кредитный калькулятор</h1>
        <Form />
        {data.schedule && !isError && <h2 className={styles.title}>График платежей</h2>}
        {isError && <p className={styles.error}>Ошибка</p>}
        {type === 'annuity'.toUpperCase() && (
          <p className={styles.text}>Ежемесячный платеж: {data.initialMonthlyPayment}</p>
        )}
        {type === 'differentiated'.toUpperCase() && (
          <>
            <p className={styles.text}>Первый платеж: {data.initialMonthlyPayment}</p>
            <p className={styles.text}>Последний платеж: {data.finalMonthlyPayment}</p>
          </>
        )}
        {data.schedule && !isError && (
          <>
            <p className={styles.text}>Общая сумма выплат: {data.totalPayment}</p>
            <div className={styles.table}>
              <Button onClick={downloadFile} style={{'marginBottom': '1.5rem'}}>Скачать</Button>
              <PaymentTable
                paymentData={data.schedule.slice(
                  (currentPage - 1) * LINES_PER_PAGE,
                  (currentPage - 1) * LINES_PER_PAGE + LINES_PER_PAGE
                )}
              />
            </div>
            <ReactPaginate
              previousLabel={<span>&#129040;</span>}
              nextLabel={<span>&#129042;</span>}
              breakLabel={'...'}
              forcePage={currentPage - 1}
              pageCount={pagesCount}
              marginPagesDisplayed={2}
              pageRangeDisplayed={3}
              onPageChange={handlePageClick}
              containerClassName={styles.pagination}
              pageClassName={styles['page-item']}
              pageLinkClassName={styles['page-link']}
              previousClassName={styles['page-item']}
              previousLinkClassName={styles['page-link']}
              nextClassName={styles['page-item']}
              nextLinkClassName={styles['page-link']}
              breakClassName={styles['page-item']}
              breakLinkClassName={styles['page-link']}
              activeClassName={styles['page-active']}
              disabledClassName={styles['page-disabled']}
            />
          </>
        )}
      </div>
    </div>
  );
});

export default Home;
