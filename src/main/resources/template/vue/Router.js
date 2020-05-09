import Container::name:: from '@/components/::name_lower::/Container::name::'

export default new Router({
    routes: [
        ...
        {
            name: 'Container::name::',
            path: '/::name_lower::',
            component: Container::name::
        },
        ...
    ],
})
