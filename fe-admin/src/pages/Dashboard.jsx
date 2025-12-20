import { MdAttachMoney, MdShoppingBag, MdPeople, MdFastfood } from 'react-icons/md';

const StatCard = ({ title, value, icon, color }) => (
    <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
        <div style={{
            width: '48px',
            height: '48px',
            borderRadius: '12px',
            backgroundColor: `${color}20`,
            color: color,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '1.5rem'
        }}>
            {icon}
        </div>
        <div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '0.2rem' }}>{title}</p>
            <h3 style={{ fontSize: '1.5rem', fontWeight: 700 }}>{value}</h3>
        </div>
    </div>
);

const Dashboard = () => {
    return (
        <div>
            <h1 className="header-title">Dashboard Overview</h1>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '1.5rem', marginBottom: '2rem' }}>
                <StatCard title="Total Revenue" value="$45,231" icon={<MdAttachMoney />} color="#238636" />
                <StatCard title="Total Orders" value="1,203" icon={<MdShoppingBag />} color="#58a6ff" />
                <StatCard title="Total Users" value="842" icon={<MdPeople />} color="#d29922" />
                <StatCard title="Menu Items" value="64" icon={<MdFastfood />} color="#da3633" />
            </div>

            <div className="card">
                <h2 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>Recent Orders</h2>
                <div style={{ padding: '2rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
                    Chart or Recent List would go here.
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
