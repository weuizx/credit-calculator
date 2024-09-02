import { makeAutoObservable, runInAction } from 'mobx';

import { BASE_URL, LINES_PER_PAGE } from '../constants';

interface FormData {
  amount: number;
  rate: number;
  term: number;
  startDate: string;
  paymentType: string;
}

export interface Payment {
  number: number;
  paymentDate: string;
  monthlyPayment: number;
  debtPayment: number;
  interestPayment: number;
  debtBalance: number;
}

interface ApiResponsePayments {
  creditId?: string;
  schedule?: Payment[];
  initialMonthlyPayment?: number;
  finalMonthlyPayment?: number;
  totalPayment?: number;
}

class CalculatorStore {
  data: ApiResponsePayments = {};
  type: string = '';
  isLoading: boolean = false;
  isError: boolean = false;
  currentPage = 1;
  pagesCount = 0;

  constructor() {
    makeAutoObservable(this);
  }

  setCurrentPage = (page: number) => {
    runInAction(() => {
      this.currentPage = page;
    });
  };

  sendPaymentsFormData = async (formData: FormData) => {
    try {
      runInAction(() => {
        this.isLoading = true;
        this.isError = false;
        this.type = '';
      });
      const response = await fetch(`${BASE_URL}/calculate-payments`, {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': BASE_URL,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        runInAction(() => {
          this.isError = true;
        });
      }
      const result: ApiResponsePayments = await response.json();
      console.log(result);
      runInAction(() => {
        this.data = result;
        this.type = formData.paymentType;
        this.isError = false;
        this.pagesCount = result.schedule ? Math.ceil(result.schedule.length / LINES_PER_PAGE) : 0;
      });
    } catch (error) {
      console.error('Ошибка при отправлении данных формы на сервер:', error);
      runInAction(() => {
        this.isError = true;
      });
    } finally {
      runInAction(() => {
        this.isLoading = false;
      });
    }
  };

  downloadFile = async () => {
    try {
      const response = await fetch(`${BASE_URL}/download-schedule?creditId=${this.data.creditId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      }
      });
      const blob = await response.blob();
      console.log('blob', blob);
      const url = window.URL.createObjectURL(blob);
      console.log('url', url);
      const link = document.createElement('a');
      link.href = url;
      const filename = 'file.xlsx';
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Ошибка при скачивании файла:', error);
    }
  };
}

const сalculatorStore = new CalculatorStore();

export default сalculatorStore;
