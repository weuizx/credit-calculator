import styles from "./Footer.module.scss";
const Footer: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={`${styles.container} container`}>
        <p className={styles.text}>
          Разработала команда 22 для <a className={styles.link} href="https://t1.ru"> T1 </a>в 2024
        </p>
      </div>
    </footer>
  );
};

export default Footer;
