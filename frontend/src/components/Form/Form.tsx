import styles from './Form.module.scss';
import Button from '../Button/Button';
import { useForm, SubmitHandler } from 'react-hook-form';
import сalculatorStore from '../../stores/calculatorStore';
import { observer } from 'mobx-react-lite';

export type Inputs = {
  amount: number;
  term: number;
  rate: number;
  startDate: string;
  paymentType: 'annuity' | 'differentiated';
  termMeasurement: 'months' | 'years';
};

const Form: React.FC = observer(() => {
  const { isLoading, sendPaymentsFormData } = сalculatorStore;

  const {
    register,
    handleSubmit,
    watch,
    setError,
    trigger,
    clearErrors,
    formState: { errors, isValid },
  } = useForm<Inputs>({
    mode: 'onChange',
    defaultValues: {
      paymentType: 'annuity',
    },
  });

  const termMeasurement = watch('termMeasurement');
  const term = watch('term');

  const handleForm: SubmitHandler<Inputs> = (data) => {
    const termInMonths = data.termMeasurement === 'years' ? data.term * 12 : data.term;
    const formattedDate = new Date(data.startDate).toISOString().slice(0, 23);

    console.log(data);
    const formData = {
      amount: data.amount,
      rate: data.rate,
      startDate: formattedDate,
      paymentType: data.paymentType.toUpperCase(),
      term: termInMonths,
    };
    sendPaymentsFormData(formData);
  };

  const handleTermMeasurementChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const termMeasurement = event.target.value;
    const value = term;
    if (!Number.isInteger(Number(value))) {
      setError('term', {
        type: 'manual',
        message: 'Введите целое число.',
      });
      return;
    }
    if (termMeasurement === 'months') {
      if (value < 1 || value > 480) {
        setError('term', {
          type: 'manual',
          message: 'Введите значение от 1 до 480 месяцев.',
        });
      } else {
        clearErrors('term');
        trigger();
      }
      return;
    }
    if (termMeasurement === 'years') {
      if (value < 1 || value > 40) {
        setError('term', {
          type: 'manual',
          message: 'Введите значение от 1 до 40 лет.',
        });
      } else {
        clearErrors('term');
        trigger();
      }
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit(handleForm)}>
      <div className={styles.content}>
        <label htmlFor="amount" className={styles.label}>
          Сумма кредита:
        </label>
        <input
          type="number"
          id="amount"
          min="0.01"
          step="0.01"
          max="1000000000000.00"
          className={styles.input}
          placeholder="1 000 000"
          {...register('amount', {
            required: 'Введите сумму кредита.',
            min: {
              value: 0.01,
              message: 'Сумма кредита не может быть меньше 0.01',
            },
            max: {
              value: 1000000000000.0,
              message: 'Cумма кредита не должна превышать 1 000 000 000 000',
            },
          })}
        />
        <p className={styles.error}>{errors.amount && errors.amount.message}</p>
        <label htmlFor="rate" className={styles.label}>
          Процентная ставка (%)
        </label>
        <input
          type="number"
          id="rate"
          min="0.01"
          step="0.01"
          max="100.00"
          className={styles.input}
          placeholder="10, 5"
          {...register('rate', {
            required: 'Введите процентную ставку.',
            min: {
              value: 0.01,
              message: 'Процентная ставка не может быть меньше 0.01',
            },
            max: {
              value: 100.0,
              message: 'Процентная ставка не должна превышать 100%',
            },
          })}
        />
        <p className={styles.error}>{errors.rate && errors.rate.message}</p>
      </div>
      <div className={styles.content}>
        <label htmlFor="term" className={styles.label}>
          Срок кредита:
        </label>
        <div className={styles['term-container']}>
          <input
            type="number"
            id="term"
            min="1"
            className={styles.input}
            placeholder="5"
            {...register('term', {
              required: 'Введите срок кредита.',
              validate: (value) => {
                if (!Number.isInteger(Number(value))) {
                  return 'Введите целое число.';
                }
                if (termMeasurement === 'months') {
                  return (value >= 1 && value <= 480) || 'Введите значение от 1 до 480 месяцев.';
                } else if (termMeasurement === 'years') {
                  return (value >= 1 && value <= 40) || 'Введите значение от 1 до 40 лет.';
                }
              },
            })}
          />
          <select
            className={styles.select}
            id="term-measurement"
            {...register('termMeasurement')}
            onChange={handleTermMeasurementChange}
          >
            <option className={styles.option} value="months">
              в месяцах
            </option>
            <option className={styles.option} value="years">
              в годах
            </option>
          </select>
        </div>
        <p className={styles.error}>{errors.term && errors.term.message}</p>
        <label htmlFor="startDate" className={styles.label}>
          Начало выплат:
        </label>
        <input
          type="date"
          id="startDate"
          className={styles.input}
          placeholder="18.08.24"
          {...register('startDate', {
            required: 'Введите дату начала выплат.',
            validate: (value) => {
              const startDate = new Date(value);
              const minDate = new Date('1600-01-01');
              const maxDate = new Date('3000-01-01');

              if (startDate < minDate || startDate > maxDate) {
                return 'Дата должна быть в диапазоне от 01.01.1600 до 01.01.3000.';
              }
            },
          })}
        />
        <p className={styles.error}>{errors.startDate && errors.startDate.message}</p>
      </div>
      <div className={styles.content}>
        <label className={styles['type-label']}>Тип платежей:</label>
        <div className={`switch-container ${styles['switch-container']}`}>
          <input
            type="radio"
            id="annuity"
            value="annuity"
            {...register('paymentType')}
            className={`switch-input ${styles['switch-input']}`}
          />
          <label htmlFor="annuity" className={`switch-label ${styles['switch-label']}`}>
            Аннуитетный
          </label>
          <input
            type="radio"
            id="differentiated"
            value="differentiated"
            {...register('paymentType')}
            className={`switch-input ${styles['switch-input']}`}
          />
          <label htmlFor="differentiated" className={`switch-label ${styles['switch-label']}`}>
            Дифференцированный
          </label>
          <div className={`switch-slider ${styles['switch-slider']}`}></div>
        </div>
        <Button
          style={{ width: '35rem', margin: 'auto' }}
          state={isLoading ? 'loading' : isValid ? 'enabled' : 'disabled'}
        >
          Рассчитать
        </Button>
      </div>
    </form>
  );
});

export default Form;
