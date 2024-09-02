import { NavLink } from 'react-router-dom';
import styles from './Header.module.scss';

const Header: React.FC = () => {
  return (
    <div className={styles.header}>
      <div className={`${styles.container} container`}>
        <NavLink to="/">
          <div className={styles.logo}>
            <div className={styles.image}></div>
            <h1 className={styles.text}>Кредит</h1>
          </div>
        </NavLink>
      </div>
    </div>
  );
};

export default Header;
