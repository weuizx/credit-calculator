import styles from './PaymentTable.module.scss';
import { Payment } from '../../stores/calculatorStore';


type PaymentTableProps = {
  paymentData?: Payment[];
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return `${(date.getDate()).toString().padStart(2, '0')}.${(date.getMonth() + 1).toString().padStart(2, '0')}.${date.getFullYear()}`;
}

const PaymentTable: React.FC<PaymentTableProps> = ({ paymentData }) => {
  return (
    <div>
      <table className={styles.table}>
        <thead className={styles['table-head']}>
          <tr>
            <th className={styles.cell}>№</th>
            <th className={styles.cell}>Дата платежа</th>
            <th className={styles.cell}>Сумма платежа</th>
            <th className={styles.cell}>Погашение долга</th>
            <th className={styles.cell}>Погашение процентов</th>
            <th className={styles.cell}>Остаток долга</th>
          </tr>
        </thead>
        <tbody className={styles['table-body']}>
          {(paymentData as Payment[]).map((row, index) => (
            <tr key={index}>
              <td className={styles.cell}>{row.number}</td>
              <td className={styles.cell}>{formatDate(row.paymentDate)}</td>
              <td className={styles.cell}>{row.monthlyPayment}</td>
              <td className={styles.cell}>{row.debtPayment}</td>
              <td className={styles.cell}>{row.interestPayment}</td>
              <td className={styles.cell}>{row.debtBalance}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PaymentTable;
